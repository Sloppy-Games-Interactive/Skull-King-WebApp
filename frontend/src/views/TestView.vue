<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'
import PlayerStatusRow from "@/components/PlayerStatusRow.vue";
import PauseMenu from "@/components/PauseMenu.vue";
import ParallaxWrapper from '@/components/ParallaxWrapper.vue'

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')
</script>

<template>
  <div class="w-full h-full">
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

    <div class="card-container">
      <ParallaxWrapper>
        <StandardCard v-if="cardType==='standard'" :suit="standardCard" :value="4"></StandardCard>
        <SpecialCard v-else :suit="specialCard"></SpecialCard>
      </ParallaxWrapper>
    </div>

    <PlayerStatusRow score=2 profile-picture="https://media.istockphoto.com/id/816752606/photo/tv-test-card-or-test-pattern-generic.jpg?s=612x612&w=0&k=20&c=63Jcx_5bFnvBw9elRDLrLKjtDYXr70pKtUK0jXJ2_uY=" username="Test"></PlayerStatusRow>
    <PauseMenu/>
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

select {
  color: black;
  width: auto;
}
</style>
