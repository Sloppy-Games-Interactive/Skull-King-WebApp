<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import Card from '@/components/cards/Card.vue'
import { type CardInterface, CardSize } from '@/core/model/Card'
import ParallaxWrapper from '@/components/utils/ParallaxWrapper.vue'
import { computed, inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useGameStateStore } from '@/core/stores/gameState'

const props = defineProps<{
  card: CardInterface | null
}>()

const show = computed(() => {
  return props.card !== null
})

const emit = defineEmits(['open', 'close'])

const close = () => {
  emit('close')
}

const gameState = useGameStateStore()
const api = inject(API_INJECTION_KEY) as ApiService
const playCard = async () => {
  if (!props.card) {
    return
  }

  const state = await api.playCard(props.card)
  gameState.updateGameState(state);
}
</script>

<template>
  <Modal :open="show" :on-click="close"
         classes="flex justify-center items-center">
    <div v-if="card" @click.stop="close()" class="play-card-container">
      <ParallaxWrapper>
        <Card :card="card" :size="CardSize.large"></Card>
      </ParallaxWrapper>
      <button
        class="btn text-5xl btn-primary m-2 bg-red-500 wood-btn" @click="playCard">play</button>
      <button
        class="btn text-5xl btn-primary m-2 bg-red-500 wood-btn" @click="close">cancel</button>
    </div>
  </Modal>
</template>

<style scoped lang="scss">
.play-card-container {
  display: inline-flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
