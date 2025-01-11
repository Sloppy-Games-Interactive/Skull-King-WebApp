<script setup lang="ts">
import {inject, ref} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerLimitInput = ref(2);
const postPlayerLimit = async () => {
  const state = await api.setPlayerLimit(playerLimitInput.value);
  gameState.updateGameState(state);
}

// TODO wait for players
</script>

<template>
  <div class="text-center gap-5">
    <h2>game lobby</h2>

    <ul>
      <li v-for="player in gameState.players">{{ player.name }}</li>
    </ul>

    <router-link to="/play" class="btn text-5xl btn-primary wood-btn"
    >Start Game</router-link
    >
    <router-link to="/join-game" class="btn text-5xl btn-primary wood-btn"
    >Back</router-link
    >
  </div>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
