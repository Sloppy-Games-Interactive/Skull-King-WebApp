<script setup lang="ts">
import { computed, inject, ref } from 'vue'
import { CardSize } from '@/core/model/Card'
import CardList from '@/components/cards/CardList.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import Modal from '@/components/utils/Modal.vue'
import { Phase } from '@/core/model/GameState'

const api = inject(API_INJECTION_KEY) as ApiService
const gameStateStore = useGameStateStore()

const isModalOpen = computed(() => {
  return (
    gameStateStore.activePlayer && gameStateStore.phase === Phase.PrepareTricks
  )
})

const setPrediction = async (prediction: number) => {
  const state = await api.setPrediction(prediction)
  gameStateStore.updateGameState(state)
}

const prediction = ref(0)

// list items numers of round
const items = computed(() => {
  const round = gameStateStore.round
  return Array.from({ length: round + 1 }, (_, i) => i)
})
</script>

<template>
  <Modal
    :open="isModalOpen ?? false"
    classes="flex justify-center items-center"
  >
    <v-card
      class="w-3/4 lg:w-1/2 mx-auto mt-10 font-sans px-5 text-center"
      title="Set your prediction"
      style="background-color: rgba(255, 255, 255, 0.5)"
    >
      <CardList
        :cards="gameStateStore.activePlayer?.hand?.cards ?? []"
        :card-size="CardSize.small"
      />

      <v-container>
        <v-autocomplete
          :items="items"
          item-text="name"
          item-value="id"
          label="Prediction"
          v-model="prediction"
          class="w-3/4 mx-auto"
        ></v-autocomplete>
      </v-container>
      <v-row justify="center">
        <v-col cols="auto" class="my-4">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="setPrediction(prediction)"
            >set prediction</v-btn
          >
        </v-col>
      </v-row>
    </v-card>
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
