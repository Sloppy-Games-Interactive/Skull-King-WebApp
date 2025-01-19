<script setup lang="ts">
import { ButtonSize } from '@/components/utils/enums'

withDefaults(
  defineProps<{
    size?: ButtonSize
    iconButton?: boolean
  }>(),
  {
    size: ButtonSize.MEDIUM,
    iconButton: false,
  },
)
</script>

<template>
  <button
    :class="`wood-btn text-${size}` + (iconButton ? ' flex align-center py-2 px-3 justify-center' : '')"
  >
    <slot></slot>
  </button>
</template>

<style scoped lang="scss"></style>
