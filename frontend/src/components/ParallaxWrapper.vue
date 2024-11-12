<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useParallax } from '@vueuse/core'

const container = ref(null)

const parallax = reactive(useParallax(container))

const parallaxStyle = computed(() => {
  return {
    '--rotate-x': `${parallax.roll * 100}deg`,
    '--rotate-y': `${parallax.tilt * 100}deg`,
    '--shine-gradient-rotation': `${60 + parallax.roll * 100}deg`,
    '--shine-position': `${Math.max(0, parallax.tilt * 100)}% ${parallax.roll * 100}%`,
    '--shadow-x': `${parallax.tilt * -100}px`,
    '--shadow-y': `${parallax.roll * 100}px`,
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
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.parallax-container > * {
  position: absolute;
  transform-origin: center;
  transform: scale(0.3) rotateX(var(--rotate-x)) rotateY(var(--rotate-y));
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
