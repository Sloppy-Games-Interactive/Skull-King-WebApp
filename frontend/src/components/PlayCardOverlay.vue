<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import Card from '@/components/cards/Card.vue'
import { type CardInterface, CardSize } from '@/core/model/Card'
import ParallaxWrapper from '@/components/utils/ParallaxWrapper.vue'
import { computed, inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import AppButton from '@/components/utils/AppButton.vue'

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

const lobby = useLobbyStore()

const api = inject(API_INJECTION_KEY) as ApiService
const playCard = async () => {
  if (!canPlay.value) {
    return
  }

  if (!props.card) {
    return
  }

  close()

  await api.playCard(props.card)
}

const canPlay = computed(() => {
  return lobby.me && lobby.me.active
})
</script>

<template>
  <Modal
    :open="show"
    :on-click="close"
    classes="flex justify-center items-center"
  >
    <div v-if="card" @click.stop="close()" class="play-card-container">
      <ParallaxWrapper @click.stop>
        <Card :card="card" :size="CardSize.large"></Card>
      </ParallaxWrapper>
      <v-container>
        <v-row justify="center">
          <v-col cols="auto">
            <AppButton
              v-if="canPlay"
              @click.stop.prevent="playCard"
            >
              Play
            </AppButton>
          </v-col>

          <v-col cols="auto">
            <AppButton @click.stop.prevent="close"> Close </AppButton>
          </v-col>
        </v-row>
      </v-container>
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
