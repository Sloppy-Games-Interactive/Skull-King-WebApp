<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import { ref } from 'vue'
import { useEventBus } from '@vueuse/core'
import { GameStateBus, GameStateEvent } from '@/core/event-bus'
import type { PlayerInterface } from '@/core/model/Player'
import { useGameStateStore } from '@/core/stores/gameState'

const open = ref(false)
const text = ref('')

const gameState = useGameStateStore()
const bus = useEventBus<GameStateEvent>(GameStateBus)

function showLastTrickWinner(player: PlayerInterface) {
  return new Promise<void>((resolve) => {
    text.value = `${player.name} won the last trick`

    open.value = true;

    setTimeout(() => {
      open.value = false
      resolve()
    }, 1000)
  })
}

function showRoundChange(round: number) {
  return new Promise<void>((resolve) => {
    text.value = `Round ${round}`

    open.value = true;

    setTimeout(() => {
      open.value = false
      resolve()
    }, 1000)
  })
}

bus.on(async (event: GameStateEvent) => {
  if (!event.oldState || !event.state) {
    return;
  }

  const state = event.state;
  const noPlayerHasPredicted = state.players.every(p => typeof p.prediction !== 'number')

  if (state.lastTrickWinner && (noPlayerHasPredicted || (gameState.currentTrick && gameState.currentTrick.stack.length === 0))) {
    await showLastTrickWinner(state.lastTrickWinner)
  }

  if (noPlayerHasPredicted && (!gameState.currentTrick)) {
    await showRoundChange(state.round)
  }
})

if (gameState.players.every(p => typeof p.prediction !== 'number') && !gameState.currentTrick) {
  showRoundChange(gameState.round)
}
</script>

<template>
  <Modal :open="open"
         classes="flex justify-center items-center"
         :z-index="100000">
    <div class="text-7xl text-white p-6 text-center">{{text}}</div>
  </Modal>
</template>

<style scoped lang="scss"></style>
