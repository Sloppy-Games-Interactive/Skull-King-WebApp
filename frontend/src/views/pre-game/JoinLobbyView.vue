<script setup lang="ts">
import { ref } from 'vue'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import AppButton from '@/components/utils/AppButton.vue'
import { useRoute } from 'vue-router'

const lobbyIdInput = ref('')
const lobbyStore = useLobbyStore()

const join = async () => {
  if (!lobbyIdInput.value.trim()) {
    return
  }

  lobbyStore.setLobbyUuid(lobbyIdInput.value.trim())

  await router.push('/join-game')
}

const route = useRoute()
const { query } = route

if (query.uuid) {
  lobbyIdInput.value = query.uuid as string
}

const rules = {
  required: (value: string) => !!value || 'Field is required',
}
</script>

<template>
  <v-card
    title="Enter lobby code"
    class="w-3/4 mx-auto mt-10 font-sans backdrop-blur-lg"
    style="background-color: rgba(255, 255, 255, 0.5)"
  >
    <v-text-field
      v-model="lobbyIdInput"
      class="w-3/4 mx-auto"
      label="Lobby join code"
      :rules="[rules.required]"
      @keydown.enter.prevent="join"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <AppButton @click.stop.prevent="join"> Next </AppButton>
        </v-col>

        <v-col cols="auto">
          <AppButton @click.stop.prevent="router.push('/')"> Cancel </AppButton>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<style scoped lang="scss"></style>
