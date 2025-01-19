<script setup lang="ts">
import PauseMenu from '@/components/PauseMenu.vue'
import Card from '@/components/cards/Card.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { type CardInterface, CardSize, StandardCard } from '@/core/model/Card'
import { ref, watch } from 'vue'
import CardList from '@/components/cards/CardList.vue'
import PredictOverlay from '@/components/PredictOverlay.vue'
import PlayCardOverlay from '@/components/PlayCardOverlay.vue'
import PhaseChangeOverlay from '@/components/PhaseChangeOverlay.vue'
import ScoreView from '@/components/ScoreView.vue'
import AppButton from '@/components/utils/AppButton.vue'
import ChatWindow from '@/components/ChatWindow.vue'
import { useChatStore } from '@/core/stores/chatStore'

const gameState = useGameStateStore()
const lobby = useLobbyStore()
const chat = useChatStore()

const randomNumbers = Array(100)
  .fill(0)
  .map(() => Math.random())

function getRandomRotationAngle(idx: number) {
  const min = -8 // minimum rotation angle
  const max = 8 // maximum rotation angle
  return (randomNumbers[idx] ?? Math.random()) * (max - min) + min
}

const playCard = ref<CardInterface | null>(null)
const showPlayCardOverlay = (card: CardInterface) => {
  playCard.value = card
}

const showChat = ref(false)
const unreadMessages = ref(false)

function openChat() {
  showChat.value = !showChat.value
  unreadMessages.value = false
}

watch(
  () => chat.messages,
  () => {
    if (!showChat.value) {
      unreadMessages.value = true
    }
  },
  { deep: true },
)
</script>

<template>
  <PhaseChangeOverlay />
  <PredictOverlay />
  <PlayCardOverlay :card="playCard as CardInterface" @close="playCard = null" />

  <div class="game-view-container w-full h-full">
    <div class="ui">
      <div class="grid grid-cols-3">
        <div class="col-span-2">
          <ScoreView />
        </div>

        <div class="justify-self-end p-3">
          <PauseMenu />
        </div>
      </div>
    </div>
    <div class="game-view-table">
      <div
        class="relative h-100 flex justify-center items-center"
        ref="container"
        :key="(gameState.currentTrick?.stack ?? []).length"
      >
        <div
          class="absolute rounded-lg bg-white/20 border-[30px] border-white/20 backdrop-blur-sm scale-[1.1]"
        >
          <div
            class="text-zinc-900 select-none text-center px-5 py-[6.5rem] rounded-lg shadow-white/10 m-[-2.5%] h-[105%] w-[105%]"
            style="
              font-family: var(--title-font-family), serif;
              box-shadow: inset 0 0 0 5px var(--tw-shadow-color);
            "
          >
            <div class="-rotate-45">
              <p class="text-4xl">SkullKing</p>
              <p>Card Game</p>
            </div>
          </div>
        </div>

        <Card
          v-for="(stackEntry, idx) in gameState.currentTrick?.stack ?? []"
          :key="
            stackEntry.player.name +
            stackEntry.card.suit +
            ((stackEntry.card as StandardCard)?.value ?? '')
          "
          class="absolute"
          :card="stackEntry.card"
          :size="CardSize.small"
          :style="{ transform: `rotate(${getRandomRotationAngle(idx)}deg` }"
        />

        <div class="absolute right-0 top-0 pr-3">
          <AppButton
            class="text-xl mb-2 relative"
            @click.stop.prevent="openChat"
            icon-button
            ><span
              v-if="unreadMessages"
              class="absolute top-[-0.25rem] left-[-0.25rem] p-2 rounded-full bg-yellow-400"
            ></span
            ><fa-icon icon="message"
          /></AppButton>
          <Transition name="slide">
            <div
              v-if="showChat"
              class="absolute top-[100%] right-0 w-[100vw] sm:max-w-[45vw] md:max-w-[30vw] h-[100vh] max-h-[20rem] px-3"
            >
              <ChatWindow class="rounded max-h-[20rem] bg-black/80" />
            </div>
          </Transition>
        </div>
      </div>
    </div>
    <div class="hand w-full align-end">
      <CardList
        :hover-effects="true"
        :class="{ 'card-list-disabled': lobby.me?.active === false }"
        :cards="lobby.me?.hand?.cards ?? []"
        :on-click="showPlayCardOverlay"
        :card-size="CardSize.medium"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.card-list-disabled {
  filter: grayscale(0.7) brightness(0.9) opacity(0.7);
}

.game-view-container {
  display: grid;
  grid-auto-flow: row;
  grid-template-areas:
    'ui ui ui'
    'table table table'
    'hand hand hand';

  row-gap: 1rem;
  grid-template-rows: auto 1fr minmax(0, 1fr);
}

.game-view-table {
  grid-area: table;
  display: grid;
}

.hand {
  grid-area: hand;
  display: grid;
}

.ui {
  grid-area: ui;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}

.slide-enter-to,
.slide-leave-from {
  transform: translateX(0);
}
</style>
