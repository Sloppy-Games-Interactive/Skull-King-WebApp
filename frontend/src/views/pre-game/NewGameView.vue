<script setup lang="ts">
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import { useGameStateStore } from '@/core/stores/gameState'
import router from '@/core/router'

const api = inject(API_INJECTION_KEY) as ApiService
const gameState = useGameStateStore()

const playerLimitInput = ref(2)
const postPlayerLimit = async () => {
  const state = await api.setPlayerLimit(playerLimitInput.value)
  gameState.updateGameState(state)
  await router.push('/join-game')
}

const rules = {
  required: (value: any) => !!value || 'Field is required',
}
</script>

<template>
  <v-card
    title="How many players?"
    class="w-3/4 mx-auto mt-10 font-sans backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <v-text-field
      v-model="playerLimitInput"
      type="number"
      class="w-3/4 mx-auto"
      label="Player count"
      :rules="[rules.required]"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="postPlayerLimit"
          >
            Next
          </v-btn>
        </v-col>

        <v-col cols="auto">
          <v-btn
            height="72"
            min-width="164"
            @click.stop.prevent="$router.push('/')"
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
