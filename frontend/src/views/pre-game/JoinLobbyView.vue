<script setup lang="ts">
import {inject, ref} from "vue";
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";
import {useGameStateStore} from "@/core/stores/gameState";
import { v4 as uuid } from 'uuid';
import router from '@/core/router'


const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore();

const playerNameInput = ref('')
const lobbyIdInput = ref('')

const join = async () => {
  if (!playerNameInput.value) {
    return
  }

  const state = await api.joinLobby(lobbyIdInput.value.toString, playerNameInput.value)
  gameState.updateGameState(state)
  await router.push('/play')
}
</script>

<template>
<v-text-field
  v-model="playerNameInput"
  class="w-3/4 mx-auto"
  label="Name"
/>
  <v-text-field
    v-model="lobbyIdInput"
    class="w-3/4 mx-auto"
    label="Lobby Id"/>

    <v-btn
      height="72"
      min-width="164"
      @click.stop.prevent="join"
    />
</template>



<style scoped lang="scss">
</style>
