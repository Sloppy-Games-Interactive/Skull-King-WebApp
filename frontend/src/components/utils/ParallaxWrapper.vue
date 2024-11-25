<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useMouseInElement, usePageLeave, useParallax, watchArray } from '@vueuse/core'

const container = ref(null)

const parallax = reactive(useParallax(container))

const enabled = ref(false)

const { isOutside } = useMouseInElement(container)
const isPageLeft = usePageLeave();

watchArray([isOutside, isPageLeft], () => {
  enabled.value = !isOutside.value && !isPageLeft.value
})

const parallaxStyle = computed(() => {
  const roll = enabled.value ? parallax.roll : 0
  const tilt = enabled.value ? parallax.tilt : 0

  return {
    'transition': enabled.value ? 'none' : 'all 0.5s',
    '--rotate-x': `${roll * 40}deg`,
    '--rotate-y': `${tilt * 40}deg`,
    '--shine-gradient-rotation': `${60 + roll * 60}deg`,
    '--shine-position': `${Math.max(0, tilt * 60)}% ${roll * 60}%`,
    '--shadow-x': `${tilt * -100}px`,
    '--shadow-y': `${roll * 100}px`,
  }
})
</script>

<template>
  <div class="parallax-container" ref="container" :style="parallaxStyle">
    <slot></slot>
  </div>
</template>

<style scoped lang="scss">
.parallax-container {
  //width: 100%;
  //height: 100%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.parallax-container > * {
  position: relative;
  transform-origin: center;
  transform: rotateX(var(--rotate-x)) rotateY(var(--rotate-y));
  box-shadow: var(--shadow-x) var(--shadow-y) 40px 0 rgba(0, 0, 0, 0.5);
  //box-shadow: var(--shadow-x) var(--shadow-y) 0 50px rgba(0, 0, 0, 1); // debug shadow

  &:after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      var(--shine-gradient-rotation),
      rgba(255, 255, 255, 0),
      rgba(255, 255, 255, 0) 47%,
      rgba(255, 255, 255, 0.05) 48%,
      rgba(255, 255, 255, 0.11) 50%,
      rgba(255, 255, 255, 0.05) 52%,
      rgba(255, 255, 255, 0) 53%,
      rgba(255, 255, 255, 0) 100%
    );
    background-size: 200% 200%;
    background-position: var(--shine-position);
    opacity: 0.5;
    pointer-events: none;
    z-index: 10;
  }
}
</style>
