<script setup lang="ts">
import { inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import AppButton from '@/components/utils/AppButton.vue'
import { ButtonSize } from '@/components/utils/enums'
import { useGameStateStore } from '@/core/stores/gameState'
import { useChatStore } from '@/core/stores/chatStore'

const lobby = useLobbyStore();
const gameState = useGameStateStore()
const chat = useChatStore()

gameState.clear()
chat.clear()

const api = inject(API_INJECTION_KEY) as ApiService

const newGame = async (event: MouseEvent) => {
  const uuid = await api.newGame()
  lobby.setLobbyUuid(uuid)
  await api.getStatus()

  await router.push('/new-game')
}
</script>

<template>
  <div class="text-center grid gap-5 px-5">
    <h1
      class="md:text-9xl sm:text-8xl text-7xl text-center text-white drop-shadow pb-10 title"
    >
      SkullKing
    </h1>

    <div class="grid justify-center gap-5">
      <AppButton @click.stop.prevent="newGame" :size="ButtonSize.LARGE"
        >New Game</AppButton>

      <AppButton @click.stop.prevent="router.push('/join-lobby')" :size="ButtonSize.LARGE"
      >Join Game</AppButton>

      <AppButton @click.stop.prevent="router.push('/settings')" :size="ButtonSize.LARGE"
        >Settings</AppButton>
    </div>
  </div>
</template>

<style scoped lang="scss">
.title {
  font-family: var(--title-font-family), serif;
}
</style>
