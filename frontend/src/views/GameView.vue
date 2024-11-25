<script setup lang="ts">
import PauseMenu from '@/components/PauseMenu.vue'
import Card from '@/components/cards/Card.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import {useGameStateStore} from '@/core/stores/gameState'
import {type CardInterface, CardSize} from '@/core/model/Card'
import { inject, ref } from 'vue'
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import CardList from "@/components/cards/CardList.vue";
import PredictOverlay from "@/components/PredictOverlay.vue";
import PlayCardOverlay from '@/components/PlayCardOverlay.vue'

const gameState = useGameStateStore()

const api = inject(API_INJECTION_KEY) as ApiService
const fetchGameStateUpdate = async () => {
  console.log("UPDATE")
  const state = await api.getStatus()
  gameState.updateGameState(state);
}

const getRandomRotationAngle = () => {
  const min = -8 // minimum rotation angle
  const max = 8 // maximum rotation angle
  return Math.random() * (max - min) + min
}

const playCard = ref<CardInterface | null>(null)
const showPlayCardOverlay = (card: CardInterface) => {
  playCard.value = card
}

const setPrediction = async() => {
  const state = await api.setPrediction(1)
  gameState.updateGameState(state);
}
</script>

<template>
  <PredictOverlay></PredictOverlay>
  <PlayCardOverlay :card="playCard as CardInterface" @close="playCard = null"></PlayCardOverlay>

  <button class="btn wood-btn" @click="fetchGameStateUpdate" id="update">update</button>
  <button class="btn wood-btn" @click="setPrediction">predict</button>
  <div class="grid grid-cols-3 md:grid-cols-6">
    <div class="col-span-3 m-4">
      <div
        v-for="player in gameState.players"
        :key="player.name"
        class="hidden sm:block"
      >
        <PlayerStatusRow
          class="m-9"
          :username="player.name"
          :score="player.score"
          profile-picture="https://media.istockphoto.com/id/816752606/photo/tv-test-card-or-test-pattern-generic.jpg?s=612x612&w=0&k=20&c=63Jcx_5bFnvBw9elRDLrLKjtDYXr70pKtUK0jXJ2_uY="
        />
      </div>
    </div>
    <div class="mr-10 mt-10 ml-auto col-end-6 lg:col-end-7">
      <PauseMenu />
    </div>
  </div>
  <!-- table -->
  <div class="flex justify-center items-center">
    <div
      class="relative h-[300px] md:h-[400px] flex justify-center items-center"
      ref="container"
    >
      <template v-for="stackEntry in gameState.currentTrick?.stack ?? []">
        <Card
          class="absolute"
          :card="stackEntry.card"
          :size="CardSize.medium"
          :style="{ transform: `rotate(${getRandomRotationAngle()}deg` }"
        ></Card>
      </template>
    </div>
  </div>
  <!-- hand cards -->
  <CardList
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
