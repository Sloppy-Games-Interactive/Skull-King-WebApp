<script setup lang="ts">
import { inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/rest/api'
import { useGameStateStore } from '@/stores/gameState'
import router from '@/router'

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
      <a @click.stop.prevent="newGame" class="btn text-5xl btn-primary wood-btn"
        >New Game</a
      >

      <a
        @click.stop.prevent="joinGame"
        class="btn text-5xl btn-primary wood-btn"
        >Join Game</a
      >

      <router-link to="/settings" class="btn text-5xl btn-primary wood-btn"
        >Settings</router-link
      >
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '../assets/buttons';

.title {
  font-family: var(--title-font-family), serif;
}

.btn {
  width: 100%;
  max-width: 400px;
}
</style>
