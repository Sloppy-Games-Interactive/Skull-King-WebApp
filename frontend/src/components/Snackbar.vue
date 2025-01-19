<script setup lang="ts">

import { reactive, ref, watchEffect } from 'vue'
import { useEventBus } from '@vueuse/core'
import { ErrorBus, ErrorEvent } from '@/core/event-bus'

const queue = reactive<string[]>([])
const alertText = ref<string | null>(null)
const displayDuration = 3000

const errorBus = useEventBus<ErrorEvent>(ErrorBus)

errorBus.on((event) => {
  if (typeof event.message === 'string') {
    queueMessage(event.message)
  } else {
    queueMessage('An error occurred')
  }
})

function queueMessage(message: string) {
  queue.push(message)
}

watchEffect(() => {
  if (alertText.value === null && queue.length > 0) {
    alertText.value = queue.shift() || null
    if (alertText.value !== null) {
      setTimeout(() => {
        alertText.value = null
      }, displayDuration)
    }
  }
})
</script>

<template>
<Teleport to="body">
  <Transition name="fade">
    <div class="absolute top-0 right-0 w-full md:w-2/5 p-3" v-if="alertText !== null && alertText.length > 0">
      <v-alert :text="alertText" color="error" class="shadow"></v-alert>
    </div>
  </Transition>
</Teleport>
</template>

<style scoped lang="scss">
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
}
</style>
