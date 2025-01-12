<script setup lang="ts">
import { inject } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'


const lobbyStore = useLobbyStore();
const api = inject(API_INJECTION_KEY) as ApiService

const newGame = async (event: MouseEvent) => {
  const uuid = await api.newGame()
  lobbyStore.setLobbyUuid(uuid)
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
      <a class="btn text-5xl btn-primary wood-btn" @click.stop.prevent="newGame"
        >New Game</a
      >
      <router-link to="/join-lobby" class="btn text-5xl btn-primary wood-btn"
        >Join Game</router-link>

      <router-link to="/settings" class="btn text-5xl btn-primary wood-btn"
        >Settings</router-link>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use 'src/assets/buttons';
.title {
  font-family: var(--title-font-family), serif;
}
</style>
