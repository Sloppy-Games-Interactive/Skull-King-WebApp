import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { watch } from 'vue'
import { GameState } from '@/core/model/GameState'
import { useWebSocket } from '@vueuse/core'
import type { JsonValue } from 'type-fest';

export function useWebsocketHandler() {
  const { status, data, send, open, close } = useWebSocket('ws://localhost:9000/ws', {
    heartbeat: {
      message: 'ping',
      interval: 1000,
      pongTimeout: 1000,
    },
    autoReconnect: {
      retries: 3,
      delay: 1000,
      onFailed() {
        alert('Failed to connect WebSocket after 3 retries')
      },
    }
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
  }

  function transportProtocol(
    event: WebSocketEvent, toClients: string[] = [""],
    fromClient: string, data: JsonValue = {}
  ): JsonValue {
    return {
      event: event.toString(),
      toClients: toClients,
      fromClient: fromClient,
      data: data
    };
  }

  const gameState = useGameStateStore()
  const lobby = useLobbyStore()

  watch(data, (newData) => {
    if (newData === 'pong') {
      return;
    }

    transportProtocol(WebSocketEvent.STATE, [], 'server')

    //console.log('newData:', newData)
    const parsedData = JSON.parse(newData);
    //console.log('parsedData:', parsedData)
    switch (parsedData.event) {
      case WebSocketEvent.CONNECTED:
        console.log('connected:', parsedData.data)
        lobby.setPlayerUuid(parsedData.data.playerId)
        break;
      case WebSocketEvent.STATE:
        //console.log('state:', parsedData.data)
        gameState.updateGameState(new GameState(parsedData.data))
        break;
      case WebSocketEvent.Message:
        //console.log('message:', parsedData.data)
        break;
      default:
      //console.log('unknown event:', parsedData.event)
    }

    // const parsedData = JSON.parse(newData);
    // if (parsedData.playerId) {
    //   parsedData.clientId = parsedData.playerId;
    //   console.log('parsedData', parsedData)
    //   delete parsedData.playerId;
    // }

    //gameState.updateGameState(new GameState(JSON.parse(newData)))
  })

  const message = () => {
    console.log('sending')
    send('state')
  }

  const sendMsgToClient = (client: string) => {
    console.log('sending')
    const data = JSON.stringify(transportProtocol(WebSocketEvent.Message,
      [client], 'server', 'hello') ?? {})
    console.log(data)
    send(data)
  }
}
