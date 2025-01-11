<script setup lang="ts">
import {ref} from "vue";
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'

const lobbyIdInput = ref('')
const lobbyStore = useLobbyStore()

const join = async () => {
  lobbyStore.setLobbyUuid(lobbyIdInput.value)

  await router.push('/join-game')
}
</script>

<template>
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
