<script setup lang="ts">
import { computed, inject, ref } from 'vue'
import { CardSize } from '@/core/model/Card'
import CardList from '@/components/cards/CardList.vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import Modal from '@/components/utils/Modal.vue'
import { Phase } from '@/core/model/GameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import AppButton from '@/components/utils/AppButton.vue'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();
const lobby = useLobbyStore()

const prediction = ref(0)

const isModalOpen = computed(() => {
  return (
    gameState.activePlayer && gameState.phase === Phase.PrepareTricks
  )
})

const setPrediction = async (p: number) => {
  await api.setPrediction(p)
  prediction.value = 0
}

const items = computed(() => {
  const round = gameState.round
  return Array.from({ length: round + 1 }, (_, i) => i)
})
</script>

<template>
  <Modal
    :open="isModalOpen ?? false"
    classes="flex justify-center items-center"
  >
    <div v-if="lobby.me?.prediction !== null" class="text-5xl text-white p-6 text-center">
      Waiting for other players
    </div>

    <v-card
      v-else
      class="w-3/4 lg:w-1/2 mx-auto mt-10 font-sans px-5 text-center"
      title="Set your prediction"
      style="background-color: rgba(255, 255, 255, 0.5)"
    >
      <CardList
        :cards="lobby.me?.hand?.cards ?? []"
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
          <AppButton
            @click.stop.prevent="setPrediction(prediction)"
            >Set prediction</AppButton
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
