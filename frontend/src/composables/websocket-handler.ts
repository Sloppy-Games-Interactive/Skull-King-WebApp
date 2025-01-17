import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { inject, ref, watch } from 'vue'
import { GameState } from '@/core/model/GameState'
import { useEventBus, useOnline, useWebSocket } from '@vueuse/core'
import type { JsonValue } from 'type-fest'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useChatStore } from '@/core/stores/chatStore'
import { ChatBus, type ChatEvent, ChatEventName } from '@/core/event-bus'
import { ChatMessage } from '@/core/model/Chat'

export function useWebsocketHandler() {
  const gameState = useGameStateStore()
  const lobby = useLobbyStore()
  const chat = useChatStore()

  const initialPlayerUuid = lobby.playerUuid

  const online = useOnline()
  const needStatusUpdate = ref(false)
  const api = inject(API_INJECTION_KEY) as ApiService
  const connectionRetryInterval = ref(1000)

  const wsUrl = import.meta.env.VITE_WS_URL

  const { status, data, send, open, close } = useWebSocket(wsUrl, {
    heartbeat: {
      message: 'ping',
      interval: 1000,
      pongTimeout: 1000,
    },
    autoReconnect: {
      retries: 3,
      delay: 1000,
    },
  })

  watch([online, status], () => {
    // backoff algorithm
    setTimeout(() => {
      if (!online.value) {
        return
      }

      if (status.value === 'CLOSED') {
        connectionRetryInterval.value = Math.min(
          connectionRetryInterval.value * 2,
          10000,
        )

        open()
        needStatusUpdate.value = true
        return
      }

      connectionRetryInterval.value = 1000
    }, connectionRetryInterval.value)
  })

  enum WebSocketEvent {
    CONNECTED = 'Connected',
    DISCONNECTED = 'Disconnected',
    STATE = 'State',
    PLAY = 'Play',
    JOIN = 'Join',
    LEAVE = 'Leave',
    Message = 'Message',
    ERROR = 'Error',
    SET_UUID = 'SetUuid',
  }

  function transportProtocol(
    event: WebSocketEvent,
    toClients: string[] = [''],
    fromClient: string,
    data: object | JsonValue = {},
  ): JsonValue {
    return {
      event: event.toString(),
      toClients: toClients,
      fromClient: fromClient,
      data: data,
    }
  }

  watch(data, newData => {
    if (newData === 'pong') {
      return
    }

    transportProtocol(WebSocketEvent.STATE, [], 'server')

    const parsedData = JSON.parse(newData)

    switch (parsedData.event) {
      case WebSocketEvent.CONNECTED:
        if (initialPlayerUuid) {
          send(
            JSON.stringify(
              transportProtocol(WebSocketEvent.SET_UUID, [], 'server', {
                lobbyUuid: lobby.lobbyUuid,
                playerUuid: initialPlayerUuid,
              }),
            ),
          )

          if (needStatusUpdate.value) {
            needStatusUpdate.value = false
            api.getStatus()
          }

          return
        }

        console.log('connected:', parsedData.data)
        lobby.setPlayerUuid(parsedData.data.playerId)
        break
      case WebSocketEvent.STATE:
        gameState.updateGameState(new GameState(parsedData.data))
        break
      case WebSocketEvent.Message:
        console.log('message:', parsedData.data)
        chat.addMessage(parsedData.data)
        break
      default:
        console.log('unknown event:', parsedData.event, parsedData)
    }

    // const parsedData = JSON.parse(newData);
    // if (parsedData.playerId) {
    //   parsedData.clientId = parsedData.playerId;
    //   console.log('parsedData', parsedData)
    //   delete parsedData.playerId;
    // }

    //gameState.updateGameState(new GameState(JSON.parse(newData)))
  })

  const chatBus = useEventBus<ChatEvent>(ChatBus)

  chatBus.on(event => {
    if (event.name === ChatEventName.Message) {
      const message = new ChatMessage({
        author: event.author,
        message: event.message,
      })

      send(
        JSON.stringify(
          transportProtocol(WebSocketEvent.Message, [], 'server', {
            lobbyUuid: lobby.lobbyUuid,
            playerUuid: initialPlayerUuid,
            data: message,
          }),
        ),
      )
    }
  })
}
