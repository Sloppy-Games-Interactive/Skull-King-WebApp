<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import {computed} from 'vue'
import {useGameStateChangeHandler} from "@/composables/gamestate-change-handler";
import { useWebSocket } from '@vueuse/core'

const route = useRoute()

useGameStateChangeHandler()

const bgClass = computed(() => route.meta?.bg)

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

const message = () => {
  console.log('sending')
  send('state')
}
</script>

<template>
  <button @click="message">send</button>

  {{data}}

  <div :class="bgClass" class="w-full h-full grid items-center">
    <RouterView />
  </div>
</template>

<style scoped></style>
