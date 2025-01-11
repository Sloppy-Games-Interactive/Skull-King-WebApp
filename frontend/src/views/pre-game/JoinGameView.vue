<script setup lang="ts">
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useGameStateStore } from '@/core/stores/gameState'
import router from '@/core/router'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore()

const playerNameInput = ref('')
const postPlayerName = async () => {
  if (!playerNameInput.value) {
    return
  }

  const state = await api.setPlayerName(playerNameInput.value)
  gameState.updateGameState(state)
  await router.push('/game-lobby')
  playerNameInput.value = ''
}

const rules = {
  required: (value: any) => !!value || 'Field is required',
}
</script>

<template>
  <v-card
    title="Enter your name"
    class="w-3/4 mx-auto mt-10 font-sans backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <v-text-field
      v-model="playerNameInput"
      class="w-3/4 mx-auto"
      label="Name"
      :rules="[rules.required]"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="postPlayerName"
          >
            Next
          </v-btn>
        </v-col>

        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="$router.push('/new-game')"
          >
            Back
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<style scoped lang="scss">
@use '../../assets/buttons';
</style>
