<script setup lang="ts">
import { ButtonSize } from '@/components/utils/enums'

withDefaults(
  defineProps<{
    size?: ButtonSize
    iconButton?: boolean
    tag?: 'button' | 'a'
  }>(),
  {
    size: ButtonSize.MEDIUM,
    iconButton: false,
    tag: 'button',
  },
)
</script>

<template>
  <component
    :is="tag"
    class="wood-btn"
    :class="[
      `text-${size}`,
      iconButton ? 'flex align-center justify-center' : '',
      {
        'py-2 px-3': iconButton && size === ButtonSize.SMALL,
        'py-3 px-4': iconButton && size === ButtonSize.MEDIUM,
        'py-4 px-5': iconButton && size === ButtonSize.LARGE,
      },
    ]"
  >
    <slot></slot>
  </component>
</template>

<style scoped lang="scss"></style>
