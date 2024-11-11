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
  <div class="text-center gap-5">
    <router-link to="/" class="btn text-5xl btn-primary wood-btn"
    >Home</router-link
    >
    <router-link to="/play" class="btn text-5xl btn-primary wood-btn"
    >Next</router-link
    >
    <router-link to="/join-game" class="btn text-5xl btn-primary wood-btn"
    >Back</router-link
    >
  </div>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
