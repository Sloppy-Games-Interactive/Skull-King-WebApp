<script setup lang="ts">
import PauseMenu from '@/components/PauseMenu.vue'
import Card from '@/components/cards/Card.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { type CardInterface, CardSize, StandardCard } from '@/core/model/Card'
import { ref } from 'vue'
import CardList from '@/components/cards/CardList.vue'
import PredictOverlay from '@/components/PredictOverlay.vue'
import PlayCardOverlay from '@/components/PlayCardOverlay.vue'
import PhaseChangeOverlay from '@/components/PhaseChangeOverlay.vue'
import ScoreView from '@/components/ScoreView.vue'
import AppButton from '@/components/utils/AppButton.vue'
import ChatWindow from '@/components/ChatWindow.vue'

const gameState = useGameStateStore()
const lobby = useLobbyStore()

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
    <div class="game-view-table align-end">
      <div
        class="relative h-[300px] md:h-[400px] flex justify-center items-center"
        ref="container"
        :key="(gameState.currentTrick?.stack ?? []).length"
      >
        <div
          class="absolute bg-white/40"
          style="transform: scale(1.1); border-radius: 8px"
        >
          <Card
            style="opacity: 0.9; filter: grayscale(0.7)"
            :card="undefined"
            :size="CardSize.small"
          />
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
            class="text-xl mb-2"
            @click.stop.prevent="showChat = !showChat"
            ><fa-icon icon="message"
          /></AppButton>
          <Transition name="slide">
            <div
              v-if="showChat"
              class="absolute top-[100%] right-0 w-[100vw] sm:max-w-[45vw] md:max-w-[30vw]"
            >
              <ChatWindow class="rounded-l max-h-[20rem] bg-black/80" />
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
        :card-size="CardSize.small"
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
  grid-template-rows: auto auto minmax(0, 1fr);
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
