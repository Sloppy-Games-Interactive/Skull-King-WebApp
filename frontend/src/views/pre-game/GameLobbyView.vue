<script setup lang="ts">
import { computed, inject, type Ref, ref, watch } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useGameStateStore } from '@/core/stores/gameState'
import { Phase } from '@/core/model/GameState'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useClipboard } from '@vueuse/core'
import AppButton from '@/components/utils/AppButton.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import ChatWindow from '@/components/ChatWindow.vue'
import { FontAwesomeIcon as FaIcon } from '@fortawesome/vue-fontawesome'
import Modal from '@/components/utils/Modal.vue'
import { useQRCode } from '@vueuse/integrations/useQRCode'
import { FRONTEND_URL } from '@/core/utils/Constants'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore()
const lobby = useLobbyStore()

const playerLimitInput = ref(2)
const postPlayerLimit = async () => {
  await api.setPlayerLimit(playerLimitInput.value)
}

// TODO implement leave lobby
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

const joinLink = computed<string | undefined>(() => {
  if (!lobby.lobbyUuid) {
    return undefined
  }

  return FRONTEND_URL + '/join-lobby?uuid=' + lobby.lobbyUuid
})

const copyBtnText = ref('Copy Join Code')

const copyLobbyIdToClipboard = () => {
  if (!joinLink.value) {
    return
  }

  const { text, copy, copied, isSupported } = useClipboard({
    source: joinLink.value,
  })

  copy(joinLink.value)

  copyBtnText.value = 'Copied!'

  setTimeout(() => {
    copyBtnText.value = 'Copy Join Code'
  }, 2000)
}

const showQrCode = ref(false)
const qrCode = ref<Ref<string, string> | null>(null)

const generateQrCode = () => {
  if (!joinLink.value) {
    return
  }
  showQrCode.value = true
  qrCode.value = useQRCode(joinLink.value)
}
</script>

<template>
  <v-card
    class="w-3/4 h-3/4 mx-auto m-10 backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <div class="text-center gap-5 _lobby-container h-full w-full p-3">
      <div
        class="_players grid align-start auto-rows-min gap-3 overflow-y-auto pr-3"
      >
        <PlayerStatusRow
          v-for="player in gameState.players"
          :key="player.id"
          :player="player"
          :show-score="false"
          :show-status="false"
          class="min-w-60"
        />
      </div>

      <div class="_chat">
        <ChatWindow fill class="rounded-l min-h-[10rem]" />
      </div>

      <Modal
        :open="showQrCode && qrCode !== null"
        classes="flex justify-center items-center"
        :on-click="() => (showQrCode = false)"
      >
        <v-card
          class="w-3/4 lg:w-1/2 mx-auto mt-10 font-sans px-5 text-center"
          title="Join Code"
          style="background-color: rgba(255, 255, 255, 0.5)"
        >
          <div class="flex justify-center">
            <img :src="qrCode?.value" alt="QR join code" />
          </div>

          <AppButton @click.stop.prevent="showQrCode = false" class="mt-4 mb-3"
            >Close</AppButton
          >
        </v-card>
      </Modal>

      <div class="_buttons">
        <div
          class="flex flex-wrap align-center justify-center gap-x-10 gap-y-3 pb-2"
        >
          <div class="absolute top-0 right-0 p-1 flex gap-3 flex-column">
            <AppButton
              @click="copyLobbyIdToClipboard"
              class="text-lg"
              icon-button
              v-tooltip:bottom="copyBtnText"
            >
              <fa-icon icon="link" />
            </AppButton>
            <AppButton
              @click="generateQrCode"
              class="text-lg"
              icon-button
              v-tooltip:bottom="'Show QR code'"
              ><fa-icon icon="qrcode"
            /></AppButton>
          </div>
          <AppButton
            v-if="lobby.isHost"
            :disabled="!hasEnoughPlayers"
            :class="{ disabled: !hasEnoughPlayers }"
            @click.stop.prevent="startGame"
            >Start Game</AppButton
          >

          <div v-else>
            <p class="text-3xl font-semibold px-3">
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

  @media (max-width: 768px) {
    grid-template-areas:
      'players'
      'chat'
      'buttons';
    grid-template-rows: auto minmax(0, 1fr) auto;
    grid-template-columns: 1fr;
  }
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
