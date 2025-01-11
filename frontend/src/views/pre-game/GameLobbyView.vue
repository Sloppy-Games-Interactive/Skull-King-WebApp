<script setup lang="ts">
import { inject, ref, computed, watch } from 'vue'
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";
import { Phase } from '@/core/model/GameState'
import router from '@/core/router'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerLimitInput = ref(2);
const postPlayerLimit = async () => {
  const state = await api.setPlayerLimit(playerLimitInput.value);
  gameState.updateGameState(state);
}

// TODO wait for players
// TODO implement leave lobby
// TODO add lobby join code for host
// TODO add lobby settings

const hasEnoughPlayers = computed<boolean>(() => {
  return gameState.players.length === gameState.playerLimit
})

watch(() => gameState.phase, (currentPhase) => {
  if (currentPhase !== Phase.PrepareGame) {
    router.push('/play');
  }
})

const startGame = async () => {
  const state = await api.startGame();
  gameState.updateGameState(state);
}
</script>

<template>
  <v-card
    title="Game Lobby"
    class="w-3/4 h-3/4 mx-auto m-10 font-sans backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <div class="text-center gap-5">
      <h2>game lobby</h2>

      <ul>
        <li v-for="player in gameState.players" :key="player.id">{{ player.name }}</li>
      </ul>

      <v-btn
        v-if="gameState.hostPlayer && gameState.me && (gameState.me.id === gameState.hostPlayer.id)"
        :disabled="!hasEnoughPlayers"
        class="btn text-5xl btn-primary wood-btn"
        :class="{'disabled': !hasEnoughPlayers}"
        @click.stop.prevent="startGame"
      >Start Game</v-btn>

      <router-link to="/" class="btn text-5xl btn-primary wood-btn"
      >Leave</router-link
      >
    </div>
  </v-card>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
