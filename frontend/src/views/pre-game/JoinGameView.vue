<script setup lang="ts">
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import router from '@/core/router'
import AppButton from '@/components/utils/AppButton.vue'

const api = inject(API_INJECTION_KEY) as ApiService

const playerNameInput = ref('')
const postPlayerName = () => {
  if (!playerNameInput.value.trim()) {
    return
  }

  api.joinLobby(playerNameInput.value).then(() => {
    router.push('/game-lobby')
  }).catch(() => {})
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
      @keydown.enter.prevent="postPlayerName"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <AppButton
            @click.stop.prevent="postPlayerName"
          >
            Next
          </AppButton>
        </v-col>

        <v-col cols="auto">
          <AppButton
            @click.stop.prevent="router.push('/')"
          >
            Cancel
        </AppButton>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<style scoped lang="scss">
</style>
