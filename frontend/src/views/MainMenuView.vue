<script setup lang="ts">
import {inject} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/rest/api";
import {useGameStateStore} from "@/stores/gameState";
import router from "@/router";

const loadGame = (event: MouseEvent) => {
  console.log('loadGame')
}

const {updateGameState} = useGameStateStore();
const api = inject(API_INJECTION_KEY) as ApiService

const newGame = async (event: MouseEvent) => {
  const state = await api.newGame();

  updateGameState(state);
  router.push('/new-game');
}
</script>

<template>
  <div class="grid grid-cols-5 grid-flow-row auto-rows-max text-center gap-5">
    <h1
      class="row-auto col-span-full text-9xl text-center text-white drop-shadow pb-10 title"
    >
      SkullKing
    </h1>

    <a
      @click.stop.prevent="newGame"
      class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn"
      >New Game</a
    >

    <a
      @click.stop.prevent="loadGame"
      class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn"
      >Load Game</a
    >

    <router-link
      to="/settings"
      class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn"
      >Settings</router-link
    >
  </div>
</template>

<style scoped lang="scss">
@use '../assets/buttons';

.title {
  font-size: 10rem;
  font-family: var(--title-font-family), serif;
}
</style>
