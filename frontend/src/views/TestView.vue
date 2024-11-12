<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { computed, reactive, ref } from 'vue'
import { useParallax } from '@vueuse/core'

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')

const container = ref(null)

const parallax = reactive(useParallax(container))

const cardStyle = computed(() => {
  return {
    'transform-origin': 'center',
    transform: `scale(0.3) rotateX(${parallax.roll * 100}deg) rotateY(${
      parallax.tilt * 100
    }deg)`,
    '--shine-gradient-rotation': `${60 + parallax.roll * 100}deg`,
    '--shine-position': `${Math.max(0, parallax.tilt * 100)}% ${parallax.roll * 100}%`,
  }
})
</script>

<template>
  <div class="w-full h-full">
    {{parallax}}
    <div>
      <select v-model="cardType">
        <option value="standard">standard</option>
        <option value="special">special</option>
      </select>
      <div v-if="cardType==='standard'">
        <select v-model="standardCard">
          <option value="blue">blue</option>
          <option value="red">red</option>
          <option value="yellow">yellow</option>
          <option value="trump">trump</option>
        </select>
      </div>
      <div v-else>
        <select v-model="specialCard">
          <option value="escape">escape</option>
          <option value="joker">joker</option>
          <option value="mermaid">mermaid</option>
          <option value="pirate">pirate</option>
          <option value="skullKing">skullKing</option>
        </select>
      </div>
    </div>

    <div class="card-container" ref="container">
      <StandardCard v-if="cardType==='standard'" :style="cardStyle" :suit="standardCard" :value="4"></StandardCard>
      <SpecialCard v-else :style="cardStyle" :suit="specialCard"></SpecialCard>
    </div>

  </div>
</template>

<style scoped lang="scss">
.card-container {
  width: 600px;
  height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card {
  position: absolute;
  transform: scale(0.3);
  transform-origin: center;

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

select {
  color: black;
  width: auto;
}
</style>
