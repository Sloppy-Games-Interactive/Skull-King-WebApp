<script setup lang="ts">
import PauseMenu from '@/components/PauseMenu.vue'
import Card from '@/components/cards/Card.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { type CardInterface, CardSize, StandardCard } from '@/core/model/Card'
import { ref } from 'vue'
import CardList from '@/components/cards/CardList.vue'
import PredictOverlay from '@/components/PredictOverlay.vue'
import PlayCardOverlay from '@/components/PlayCardOverlay.vue'
import PlayerStatusMenu from "@/components/PlayerStatusMenu.vue";

const gameState = useGameStateStore()

const randomNumbers = Array(100)
  .fill(0)
  .map(() => Math.random())

const getRandomRotationAngle = (idx: number) => {
  const min = -8 // minimum rotation angle
  const max = 8 // maximum rotation angle
  return (randomNumbers[idx] ?? Math.random()) * (max - min) + min
}

const playCard = ref<CardInterface | null>(null)
const showPlayCardOverlay = (card: CardInterface) => {
  playCard.value = card
}
</script>

<template>
  <PredictOverlay></PredictOverlay>
  <PlayCardOverlay
    :card="playCard as CardInterface"
    @close="playCard = null"
  ></PlayCardOverlay>

  <div class="grid grid-cols-3 md:grid-cols-6">
    <div class="ml-10 mt-10 ml-auto fixed left-0 top-0 align-middle z-[9]">
      <PlayerStatusMenu />
    </div>


    <div class="mr-10 mt-10 ml-auto fixed right-0 top-0 align-middle z-[10]">
      <PauseMenu />
    </div>
  </div>
  <!-- table -->
  <div class="flex justify-center items-center">
    <div
      class="relative h-[300px] md:h-[400px] flex justify-center items-center"
      ref="container"
      :key="(gameState.currentTrick?.stack ?? []).length"
    >
      <Card
        v-for="(stackEntry, idx) in gameState.currentTrick?.stack ?? []"
        :key="
          stackEntry.player.name +
          stackEntry.card.suit +
          ((stackEntry.card as StandardCard)?.value ?? '')
        "
        class="absolute"
        :card="stackEntry.card"
        :size="CardSize.medium"
        :style="{ transform: `rotate(${getRandomRotationAngle(idx)}deg` }"
      ></Card>
    </div>
  </div>
  <!-- hand cards -->
  <CardList
    hover-effects="true"
    :cards="gameState.activePlayer?.hand?.cards ?? []"
    :on-click="showPlayCardOverlay"
  />
</template>

<style scoped lang="scss">
@use 'src/assets/buttons';

select {
  color: black;
  width: auto;
}
</style>
