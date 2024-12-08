<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import { computed, watch } from 'vue'
import {useGameStateChangeHandler} from "@/composables/gamestate-change-handler";
import { useWebSocket } from '@vueuse/core'
import { useGameStateStore } from '@/core/stores/gameState'
import type { JsonValue } from 'type-fest';
import { v4 as uuid } from 'uuid';

import { GameState } from '@/core/model/GameState'

const route = useRoute()

useGameStateChangeHandler()

const bgClass = computed(() => route.meta?.bg)

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

function transportProtocol(event: WebSocketEvent, toClients: string[], fromClient: string, data: JsonValue): JsonValue {
  return {
    event: event.toString(),
    toClients: toClients,
    fromClient: fromClient,
    data: data
  };
}

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


const gameState = useGameStateStore()

watch(data, (newData) => {
  if (newData === 'pong') {
    return;
  }

  console.log('newData:', newData)
  const parsedData = JSON.parse(newData);

  switch (parsedData.event) {
    case WebSocketEvent.STATE:
      gameState.updateGameState(new GameState(parsedData.data))
      break;
    case WebSocketEvent.Message:
      console.log('message:', parsedData.data)
      break;
    default:
      console.log('unknown event:', parsedData.event)
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

const test = () => {
  console.log('test')
  send('test')
}

const sendmsgtoclient = (client: string) => {
  console.log('sending')
  const data = JSON.stringify(transportProtocol(WebSocketEvent.Message, [client], 'server', 'hello') ?? {})
  console.log(data)
  send(data)
}
</script>

<template>
  <button @click="message">send</button>
  <button @click="test">test</button>
  <form>
    <input type="text" v-model="client" />
    <button @click="sendmsgtoclient(client)">send to client</button>
  </form>
  {{data}}

  <div :class="bgClass" class="w-full h-full grid items-center">
    <RouterView />
  </div>
</template>

<style scoped></style>
