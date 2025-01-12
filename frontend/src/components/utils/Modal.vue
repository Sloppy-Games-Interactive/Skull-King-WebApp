<script setup lang="ts">
withDefaults(defineProps<{
  open: boolean,
  classes?: string,
  onClick?: () => void,
  zIndex?: number
}>(), {
  open: () => false,
  classes: () => '',
  zIndex: () => 999
})
</script>

<template>
<Teleport to="body">
  <Transition name="fade">
    <div v-if="open" class="modal-backdrop" :class="classes + ` z-[${zIndex}]`" @click.stop.prevent="onClick">
      <slot></slot>
    </div>
  </Transition>
</Teleport>
</template>

<style scoped lang="scss">
.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.3s ease,
    backdrop-filter 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  backdrop-filter: blur(0);
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  backdrop-filter: blur(20px);
}

.modal-backdrop {
  z-index: 999;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  backdrop-filter: blur(20px);
}
</style>
