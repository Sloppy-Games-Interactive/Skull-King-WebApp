<script setup lang="ts">
import {inject, ref} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/rest/api";
import {useGameStateStore} from "@/stores/gameState";

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerLimitInput = ref(2);
const postPlayerLimit = async () => {
  const state = await api.setPlayerLimit(playerLimitInput.value);
  gameState.updateGameState(state);
}
</script>

<template>
  <div class="grid grid-cols-5 grid-flow-row auto-rows-max text-center gap-5">
    <div>round: {{ gameState.round }}</div>
    <div>players: {{gameState.players}}</div>
    <div>player limit: {{gameState.playerLimit}}</div>
    <br>
    <br>
    <br>
    <input v-model="playerLimitInput" placeholder="player limit">
    <button @click.stop.prevent="postPlayerLimit">submit</button>

    <br>
    <br>
    <br>
    <router-link
      to="/"
      class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn"
      >Go Back</router-link
    >
  </div>
</template>

<style scoped lang="scss">
@use '../assets/buttons';
</style>
