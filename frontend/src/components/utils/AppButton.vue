<script setup lang="ts">
import { ButtonSize } from '@/components/utils/enums'

withDefaults(
  defineProps<{
    size?: ButtonSize;
  }>(),
  {
    size: ButtonSize.MEDIUM,
  },
);
</script>

<template>
<button :class="`wood-btn text-${size}`">
  <slot></slot>
</button>
</template>

<style scoped lang="scss">

</style>
