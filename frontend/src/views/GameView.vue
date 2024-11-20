<script setup lang="ts">
import PauseMenu from '@/components/PauseMenu.vue'
import Card from '@/components/cards/Card.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import { useGameStateStore } from '@/stores/gameState'
import {type CardInterface, CardSize} from '@/model/Card'
import {inject} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/rest/api";

const gameState = useGameStateStore()

const api = inject(API_INJECTION_KEY) as ApiService
const fetchGameStateUpdate = async () => {
  const state = await api.getStatus()
  gameState.updateGameState(state);
}

const getRandomRotationAngle = () => {
  const min = -8 // minimum rotation angle
  const max = 8 // maximum rotation angle
  return Math.random() * (max - min) + min
}

const playCard = async (card: CardInterface) => {
  const state = await api.playCard(card)
  gameState.updateGameState(state);
}

const setPrediction = async() => {
  const state = await api.setPrediction(1)
  gameState.updateGameState(state);
}
</script>

<template>
  <button class="btn wood-btn" @click="fetchGameStateUpdate">update</button>
  <button class="btn wood-btn" @click="setPrediction">predict</button>
  <div class="w-full h-full">
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

    <div class="flex justify-center items-center">
      <div
        class="relative h-[500px] w-[300px] md:h-[600px] md:w-[600px] flex justify-center items-center"
        ref="container"
      >
        <template v-for="stackEntry in gameState.currentTrick?.stack ?? []">
          <Card class="absolute" :card="stackEntry.card" :size="CardSize.medium" :style="{ transform: `rotate(${getRandomRotationAngle()}deg` }"></Card>
        </template>
      </div>
    </div>

    <!-- hand cards -->
    <div class="relative w-[100vw]">
      <div class="flex justify-center -space-x-16 overflow-x-auto overflow-y-visible">
        <template v-for="card in gameState.activePlayer?.hand?.cards ?? []">
          <Card :card="card" :size="CardSize.medium" @click="playCard(card)"></Card>
        </template>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use 'src/assets/buttons';
select {
  color: black;
  width: auto;
}
</style>
