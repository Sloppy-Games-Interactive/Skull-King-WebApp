<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import { computed } from 'vue'
import { useGameStateChangeHandler } from '@/composables/gamestate-change-handler'
import { useWebsocketHandler } from '@/composables/websocket-handler'
import { useEventBus, useOnline } from '@vueuse/core'
import { ErrorBus, ErrorEvent } from '@/core/event-bus'
import { ref } from 'vue'
import Modal from '@/components/utils/Modal.vue'
import Snackbar from '@/components/Snackbar.vue'

const route = useRoute()

useGameStateChangeHandler()

const bgClass = computed(() => route.meta?.bg)

useWebsocketHandler()

const errorBus = useEventBus<ErrorEvent>(ErrorBus)
const errorMsg = ref('')
const showError = ref(false)

errorBus.on(event => {
  if (typeof event.message === 'string') {
    console.log('has message')
    errorMsg.value = event.message
  } else {
    console.log('has no message')
    errorMsg.value = 'An error occurred'
  }

  showError.value = true
})

const online = useOnline()
</script>

<template>
  <Modal :open="!online" classes="flex justify-center items-center">
    <div
      class="text-5xl text-white p-6 text-center"
    >
      You are offline. Please check your internet connection.
    </div>
  </Modal>

  <Snackbar />

  <div :class="bgClass" class="w-full h-full grid items-center">
    <RouterView />
  </div>
</template>

<style scoped></style>
