<script setup lang="ts">
import { inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/rest/api'
import { useGameStateStore } from '@/stores/gameState'
import router from '@/router'
import WoodButton from "@/components/WoodButton.vue";

const joinGame = (event: MouseEvent) => {
  console.log('joinGame')
}

const { updateGameState } = useGameStateStore()
const api = inject(API_INJECTION_KEY) as ApiService

const newGame = async (event: MouseEvent) => {
  // const state = await api.newGame()

  // updateGameState(state)
  await router.push('/new-game')
}
</script>

<template>
  <div class="text-center grid gap-5 px-5">
    <h1 class="md:text-9xl sm:text-8xl text-7xl text-center text-white drop-shadow pb-10 title">
      SkullKing
    </h1>

    <div class="grid justify-center gap-5">
      <WoodButton text="New Game" :func="newGame" :use-router-link="false"/>

      <WoodButton text="Join Game" :func="joinGame" :use-router-link="false"/>

      <WoodButton text="Settings" url="/settings" :use-router-link="true"/>
    </div>
  </div>
</template>

<style scoped lang="scss">
.title {
  font-family: var(--title-font-family), serif;
}
</style>
