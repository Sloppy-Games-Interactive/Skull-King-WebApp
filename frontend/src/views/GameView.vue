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
import PlayerStatusMenu from "@/components/PlayerStatusMenu.vue";
import PhaseChangeOverlay from '@/components/PhaseChangeOverlay.vue'
import ScoreView from '@/components/ScoreView.vue'


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

//todo get sessionstorage
//gameStore.updateGameState
//if not working: nextTick?
</script>

<template>
  <KeepAlive><PhaseChangeOverlay :key="0" /></KeepAlive>
  <PredictOverlay></PredictOverlay>
  <PlayCardOverlay
    :card="playCard as CardInterface"
    @close="playCard = null"
  ></PlayCardOverlay>


  <div class="game-view-container w-full h-full">
    <div class="ui">
      <div class="grid grid-cols-3">
        <div class="col-span-2">
          <ScoreView/>
        </div>

        <div class="justify-self-end p-3">
          <PauseMenu/>
        </div>

      </div>
    </div>
    <div class="game-view-table">
      <div
        class="relative h-[300px] md:h-[400px] flex justify-center items-center"
        ref="container"
        :key="(gameState.currentTrick?.stack ?? []).length"
      >
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
        ></Card>
      </div>
    </div>
    <div class="hand w-full">
      <CardList
        :hover-effects="true"
        :cards="lobby.me?.hand?.cards ?? []"
        :on-click="showPlayCardOverlay"
        :card-size="CardSize.small"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
select {
  color: black;
  width: auto;
}

.game-view-container {  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 3fr 3fr;
  gap: 0px 0px;
  grid-auto-flow: row;
  grid-template-areas:
    "ui ui ui"
    "table table table"
    "hand hand hand";
}

.game-view-table { grid-area: table; }

.hand { grid-area: hand; }

.ui { grid-area: ui; }

</style>
