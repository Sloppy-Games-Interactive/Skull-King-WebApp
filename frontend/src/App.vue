<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import { computed } from 'vue'
import {useGameStateChangeHandler} from "@/composables/gamestate-change-handler";
import { useWebsocketHandler } from '@/composables/websocket-handler'
import { useEventBus } from '@vueuse/core'
import { ErrorBus, ErrorEvent } from '@/core/event-bus'
import { ref } from 'vue'


const route = useRoute()

useGameStateChangeHandler();

const bgClass = computed(() => route.meta?.bg)

useWebsocketHandler()

const errorBus = useEventBus<ErrorEvent>(ErrorBus)
const errorMsg = ref('')
const showError = ref(false)

errorBus.on((event) => {
  if (typeof event.message === 'string') {
    console.log('has message')
    errorMsg.value = event.message
  } else {
    console.log('has no message')
    errorMsg.value = 'An error occurred'
  }

  showError.value = true
})

</script>

<template>
  <div :class="bgClass" class="w-full h-full grid items-center">
    <RouterView />
  </div>
</template>

<style scoped></style>
