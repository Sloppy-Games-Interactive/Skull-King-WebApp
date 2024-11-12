<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'
import ParallaxWrapper from '@/components/ParallaxWrapper.vue'

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')
</script>

<template>
  <div class="w-full h-full">
    <div class="absolute">
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

    <ParallaxWrapper>
      <StandardCard v-if="cardType==='standard'" :suit="standardCard" :value="4"></StandardCard>
      <SpecialCard v-else :suit="specialCard"></SpecialCard>
    </ParallaxWrapper>
  </div>
</template>

<style scoped lang="scss">
select {
  color: black;
  width: auto;
}
</style>
