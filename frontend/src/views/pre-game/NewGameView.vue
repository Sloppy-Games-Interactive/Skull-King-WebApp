<script setup lang="ts">
import {inject, ref} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";
import router from "@/core/router";

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerLimitInput = ref(2);
const postPlayerLimit = async () => {
  const state = await api.setPlayerLimit(playerLimitInput.value);
  gameState.updateGameState(state);
  await router.push('/join-game');
}
</script>

<template>
  <div class="text-center gap-5">
   <input id="player-limit" type="number" v-model="playerLimitInput" class="text-5xl text-center text-black w-1/4 mx-auto rounded-2xl"/>
  </div>
  <div class="flex flex-col items-center w-3/4 mx-auto">
    <a class="btn text-5xl btn-primary wood-btn" @click.stop.prevent="postPlayerLimit">Next</a>
    <router-link to="/" class="btn text-5xl btn-primary wood-btn">Back</router-link>
  </div>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
