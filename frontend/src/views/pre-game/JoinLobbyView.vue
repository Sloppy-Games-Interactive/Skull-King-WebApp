<script setup lang="ts">
import {ref} from "vue";
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'

const lobbyIdInput = ref('')
const lobbyStore = useLobbyStore()

const join = async () => {
  if (!lobbyIdInput.value.trim()) {
    return;
  }

  lobbyStore.setLobbyUuid(lobbyIdInput.value.trim())

  await router.push('/join-game')
}
</script>

<template>

  <v-card
    title="Enter lobby id"
    class="w-3/4 mx-auto mt-10 font-sans backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <v-text-field
      v-model="lobbyIdInput"
      class="w-3/4 mx-auto"
      label="Lobby UUID"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="join"
          >
            Next
          </v-btn>
        </v-col>

        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="router.push('/')"
          >
            Cancel
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>



<style scoped lang="scss">
</style>
