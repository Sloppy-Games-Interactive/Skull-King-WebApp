<script setup lang="ts">
import {inject, ref} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/rest/api";
import {useGameStateStore} from "@/stores/gameState";
import router from "@/router";

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerNameInput = ref('');
const postPlayerName = async () => {
  const state = await api.setPlayerName(playerNameInput.value);
  gameState.updateGameState(state);
  if (state.players.length === state.playerLimit) {
    await router.push('/game-lobby');
    return;
  }
  playerNameInput.value = '';
}
</script>

<template>
  <div class="text-center gap-5 flex flex-col">
    <input class="text-5xl text-center text-black w-1/4 mx-auto rounded-2xl" v-model="playerNameInput">
    <a class="btn text-5xl btn-primary wood-btn" @click.stop.prevent="postPlayerName">Next</a>


    <router-link to="/new-game" class="btn text-5xl btn-primary wood-btn"
    >Back</router-link
    >
  </div>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
