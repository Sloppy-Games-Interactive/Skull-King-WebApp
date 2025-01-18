<script setup lang="ts">
import { computed, inject, ref, watch } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useGameStateStore } from '@/core/stores/gameState'
import { Phase } from '@/core/model/GameState'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useClipboard, useEventBus } from '@vueuse/core'
import AppButton from '@/components/utils/AppButton.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import { useChatStore } from '@/core/stores/chatStore'
import { ChatBus, ChatEvent, ChatEventName } from '@/core/event-bus'
import ChatWindow from '@/components/ChatWindow.vue'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore()
const lobby = useLobbyStore()
const chat = useChatStore()
const chatBus = useEventBus<ChatEvent>(ChatBus)

const message = ref('')
const sendMessage = () => {
  if (!lobby.me || message.value.trim().length === 0) {
    return
  }
  chatBus.emit(
    new ChatEvent(ChatEventName.Message, lobby.me, message.value.trim()),
  )
}

const playerLimitInput = ref(2)
const postPlayerLimit = async () => {
  await api.setPlayerLimit(playerLimitInput.value)
}

// TODO wait for players
// TODO implement leave lobby
// TODO add lobby join code for host
// TODO add lobby settings

const hasEnoughPlayers = computed<boolean>(() => {
  return gameState.players.length === gameState.playerLimit
})

watch(
  () => gameState.phase,
  currentPhase => {
    if (currentPhase !== Phase.PrepareGame) {
      router.push('/play')
    }
  },
)

const startGame = async () => {
  await api.startGame()
}

const copyBtnText = ref('copy uuid')
const { text, copy, copied, isSupported } = useClipboard({
  source: lobby.lobbyUuid,
})

const copyLobbyIdToClipboard = () => {
  copy(lobby.lobbyUuid)

  copyBtnText.value = 'copied'

  setTimeout(() => {
    copyBtnText.value = 'copy uuid'
  }, 2000)
}
</script>

<template>
  <v-card
    class="w-3/4 h-3/4 mx-auto m-10 backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <div class="text-center gap-5 _lobby-container h-full w-full p-3">
      <!--      <h1 class="text-6xl font-bold pt-10">Game Lobby</h1>-->

      <div class="grid place-items-center my-8 _players">
        <PlayerStatusRow
          v-for="(player, idx) in gameState.players"
          :key="player.id"
          :player="player"
          :show-score="false"
          class="min-w-[15em] my-5"
        />
      </div>

      <div class="_chat">
        <ChatWindow fill class="rounded-l" />
      </div>

      <div class="_buttons">
        <div class="grid grid-flow-col align-center justify-center gap-10 pb-2">
          <AppButton @click="copyLobbyIdToClipboard">
            {{ copyBtnText }}
          </AppButton>
          <AppButton
            v-if="lobby.isHost"
            :disabled="!hasEnoughPlayers"
            :class="{ disabled: !hasEnoughPlayers }"
            @click.stop.prevent="startGame"
            >Start Game</AppButton
          >

          <div v-else>
            <p class="text-2xl font-semibold px-3">
              Waiting for host to start the game
            </p>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<style scoped lang="scss">
._lobby-container {
  display: grid;
  grid-template-areas:
    'players chat'
    'buttons buttons';
  grid-template-rows: minmax(0, 1fr) auto;
  grid-template-columns: auto 1fr;
}

._players {
  grid-area: players;
}

._chat {
  grid-area: chat;
}

._buttons {
  grid-area: buttons;
}
</style>
