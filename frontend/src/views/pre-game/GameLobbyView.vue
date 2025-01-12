<script setup lang="ts">
import { inject, ref, computed, watch } from 'vue'
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";
import { Phase } from '@/core/model/GameState'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useClipboard } from '@vueuse/core'
import AppButton from '@/components/utils/AppButton.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'

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

const copyBtnText = ref('copy uuid')
const { text, copy, copied, isSupported } = useClipboard({ source: lobby.lobbyUuid })

const copyLobbyIdToClipboard = () => {
  copy(lobby.lobbyUuid);

  copyBtnText.value = 'copied'

  setTimeout(() => {
    copyBtnText.value = 'copy uuid'
  }, 2000)
}
</script>

<template>
  <v-card
    class="w-3/4 h-3/4 mx-auto m-10 backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5);"
  >
    <div class="text-center gap-5">
      <h1 class="text-6xl font-bold pt-10">Game Lobby</h1>

      <div class="grid place-items-center my-8">
        <PlayerStatusRow v-for="player in gameState.players" :key="player.id"
                         :profile-picture="player.profilePicUrl"
                         :username="player.name" class="min-w-[15em] my-5"/>
      </div>

      <AppButton @click="copyLobbyIdToClipboard" style="margin-bottom: 50px;">
        {{ copyBtnText }}
      </AppButton>


      <div>
        <AppButton
          v-if="lobby.isHost"
          :disabled="!hasEnoughPlayers"
          :class="{'disabled': !hasEnoughPlayers}"
          @click.stop.prevent="startGame"
        >Start Game</AppButton>

        <div v-else>
          <p class="text-2xl font-semibold">Waiting for host to start the game</p>
        </div>
      </div>
    </div>
  </v-card>
</template>

<style scoped lang="scss">
</style>
