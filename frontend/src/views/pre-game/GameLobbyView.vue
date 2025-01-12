<script setup lang="ts">
import { inject, ref, computed, watch } from 'vue'
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";
import { Phase } from '@/core/model/GameState'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useClipboard } from '@vueuse/core'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();
const lobby = useLobbyStore()

const playerLimitInput = ref(2);
const postPlayerLimit = async () => {
  await api.setPlayerLimit(playerLimitInput.value);
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
  await api.startGame();
}

const { text, copy, copied, isSupported } = useClipboard({ source: lobby.lobbyUuid })
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

      <button @click="copy(lobby.lobbyUuid)" style="margin-bottom: 50px;">
        copy uuid
      </button>


      <div>
        <v-btn
          v-if="lobby.isHost"
          :disabled="!hasEnoughPlayers"
          class="btn text-5xl btn-primary wood-btn"
          :class="{'disabled': !hasEnoughPlayers}"
          @click.stop.prevent="startGame"
        >Start Game</v-btn>

        <router-link to="/" class="btn text-5xl btn-primary wood-btn"
        >Leave</router-link
        >
      </div>
    </div>
  </v-card>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
