<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import { ref } from 'vue'
import { useEventBus } from '@vueuse/core'
import { GameStateBus, GameStateEvent } from '@/core/event-bus'
import { Phase } from '@/core/model/GameState'
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
  const oldState = event.oldState

  const noPlayerHasPredicted = state.players.every(p => typeof p.prediction !== 'number')
  if (state.lastTrickWinner && (noPlayerHasPredicted || (gameState.currentTrick && gameState.currentTrick.stack.length === 0))) {
    await showLastTrickWinner(state.lastTrickWinner)
  }
  // val noPlayerHasPredicted = controller.state.players.forall(p => p.prediction.isEmpty)
  // val activeTrick = controller.state.activeTrick
  // if (noPlayerHasPredicted || (activeTrick.isDefined && activeTrick.get.stack.isEmpty)) {
  //   displayTrickWinnerModal()
  // }

  // val noPlayerHasPredicted = controller.state.players.forall(p => p.prediction.isEmpty)
  // if (controller.state.tricks.isEmpty && noPlayerHasPredicted) {

  const isEnteringPrepareTricks = state.phase === Phase.PrepareTricks
  const isRoundChange = state.round !== oldState.round

  console.log('PHASE OVERLAY', event)
  if (noPlayerHasPredicted && (!gameState.currentTrick)) {
    await showRoundChange(state.round)
  }
})
</script>

<template>
  <Modal :open="open"
         classes="flex justify-center items-center"
         :z-index="10000">
    <div class="text-5xl text-white">{{text}}</div>
  </Modal>
</template>

<style scoped lang="scss"></style>
