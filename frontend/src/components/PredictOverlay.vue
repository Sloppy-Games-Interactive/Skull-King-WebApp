<script setup lang="ts">
import { computed, inject } from 'vue'
import { CardSize } from '@/core/model/Card'
import VCardList from '@/components/cards/CardList.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import Modal from '@/components/utils/Modal.vue'
import { Phase } from '@/core/model/GameState'

const api = inject(API_INJECTION_KEY) as ApiService
const gameStateStore = useGameStateStore()

defineOptions({
  name: 'VPredictOverlay',
})

const isModalOpen = computed(() => {
  return (
    gameStateStore.activePlayer && gameStateStore.phase === Phase.PrepareTricks
  )
})

const setPrediction = async (prediction: number) => {
  const state = await api.setPrediction(prediction)
  gameStateStore.updateGameState(state)
}
</script>

<template>
  <Modal
    :open="isModalOpen ?? false"
    classes="flex justify-center items-center"
  >
    <div
      @click.stop.prevent
      class="bg-blue-900 w-3/4 rounded-xl justify-center place-center text-center"
    >
      <h1 class="text-5xl">Set your prediction</h1>
      <VCardList
        :cards="gameStateStore.activePlayer?.hand?.cards ?? []"
        :card-size="CardSize.small"
      />
      <div class="">
        <template v-for="n in gameStateStore.round">
          <button
            class="btn text-5xl btn-primary m-2 bg-red-500 wood-btn"
            @click="setPrediction(n)"
          >
            {{ n }}
          </button>
        </template>
      </div>
    </div>
  </Modal>
</template>

<style scoped lang="scss">
//@use 'src/assets/buttons';

.menu {
  z-index: 1000;
  position: fixed;
  top: 0;
  right: 0;
}
</style>
