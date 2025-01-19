<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import { ref } from 'vue'
import { useEventBus } from '@vueuse/core'
import { GameStateBus, GameStateEvent } from '@/core/event-bus'
import type { PlayerInterface } from '@/core/model/Player'
import { useGameStateStore } from '@/core/stores/gameState'
import { Phase } from '@/core/model/GameState'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import AppButton from '@/components/utils/AppButton.vue'

const open = ref(false)
const text = ref('')
const showScores = ref(false)
const gameWinners = ref<PlayerInterface[] | null>(null)

const gameState = useGameStateStore()
const bus = useEventBus<GameStateEvent>(GameStateBus)

function showLastTrickWinner(player: PlayerInterface) {
  return new Promise<void>(resolve => {
    text.value = `${player.name} won the last trick`
    showScores.value = true
    open.value = true

    setTimeout(() => {
      open.value = false
      resolve()
    }, 3000)
  })
}

function showRoundChange(round: number) {
  return new Promise<void>(resolve => {
    text.value = `Round ${round}`
    showScores.value = false
    open.value = true

    setTimeout(() => {
      open.value = false
      resolve()
    }, 2000)
  })
}

function showEndGameScreen() {
  gameWinners.value = gameState.players.reduce((acc, player) => {
    if (acc.length === 0 || player.score === acc[0].score) {
      acc.push(player)
    } else if (player.score > acc[0].score) {
      acc = [player]
    }
    return acc
  }, [] as PlayerInterface[])

  if (gameWinners.value.length === 1) {
    text.value = `${gameWinners.value[0].name} won the game with ${gameWinners.value[0].score} points!`
  } else {
    text.value = `It's a tie between ${gameWinners.value.map(p => p.name).join(' and ')} with ${gameWinners.value[0].score} points!`
  }

  showScores.value = true
  open.value = true
}

bus.on(async (event: GameStateEvent) => {
  if (!event.state) {
    return
  }

  const state = event.state

  const noPlayerHasPredicted = state.players.every(
    p => typeof p.prediction !== 'number',
  )

  if (
    state.lastTrickWinner &&
    (noPlayerHasPredicted ||
      (gameState.currentTrick && gameState.currentTrick.stack.length === 0))
  ) {
    await showLastTrickWinner(state.lastTrickWinner)
  }

  if (state.phase === Phase.EndGame) {
    if (state.lastTrickWinner) {
      await showLastTrickWinner(state.lastTrickWinner)
    }

    showEndGameScreen()
    return
  }

  if (noPlayerHasPredicted && !gameState.currentTrick) {
    await showRoundChange(state.round)
  }
})

if (
  gameState.players.every(p => typeof p.prediction !== 'number') &&
  !gameState.currentTrick
) {
  showRoundChange(gameState.round)
}
</script>

<template>
  <Modal
    :open="open"
    classes="flex justify-center items-center"
    :z-index="100000"
  >
    <div class="grid gap-3">
      <div class="text-7xl text-white p-6 text-center break-words overflow-hidden">{{ text }}</div>

      <div
        v-if="showScores"
        class="grid gap-3 overflow-y-auto px-3 pb-3 max-h-[75vh] grid-cols-1 md:grid-cols-2 place-items-center"
      >
        <template v-for="player in gameState.players" :key="player.id">
          <PlayerStatusRow
            :player="player"
            :show-score="true"
            :show-status="gameWinners === null || gameWinners.length === 0"
          />
        </template>
      </div>

      <div v-if="gameWinners !== null && gameWinners.length > 0" class="flex justify-center">
        <AppButton @click="open = false">Close</AppButton>
      </div>
    </div>
  </Modal>
</template>

<style scoped lang="scss"></style>
