<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')
</script>

<template>
  <div>
    <select v-model="cardType">
      <option value="standard">standard</option>
      <option value="special">special</option>
    </select>
  </div>

  <template v-if="cardType==='standard'">
    <div>
      <select v-model="standardCard">
        <option value="blue">blue</option>
        <option value="red">red</option>
        <option value="yellow">yellow</option>
        <option value="trump">trump</option>
      </select>
    </div>

    <StandardCard :suit="standardCard" :value="4"></StandardCard>
  </template>
  <template v-else>
    <div>
      <select v-model="specialCard">
        <option value="escape">escape</option>
        <option value="joker">joker</option>
        <option value="mermaid">mermaid</option>
        <option value="pirate">pirate</option>
        <option value="skullKing">skullKing</option>
      </select>
    </div>

    <SpecialCard :suit="specialCard"></SpecialCard>
  </template>
</template>

<style scoped lang="scss">
.card {
  position: absolute;
  transform: scale(0.5);
  transform-origin: center;
}

select {
  color: black;
  width: auto;
}
</style>
