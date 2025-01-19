<script setup lang="ts">
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import router from '@/core/router'
import AppButton from '@/components/utils/AppButton.vue'

const api = inject(API_INJECTION_KEY) as ApiService

const playerLimitInput = ref(2)
const postPlayerLimit = async () => {
  await api.setPlayerLimit(Number.parseInt(playerLimitInput.value))
  await router.push('/join-game')
}

const rules = {
  required: (value: string) => !!value || 'Field is required',
  maxValue: (value: string) =>
    Number.parseInt(value) <= 10 || 'Max value is 10',
  minValue: (value: string) => Number.parseInt(value) >= 2 || 'Min value is 2',
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
      :rules="[rules.required, rules.maxValue, rules.minValue]"
      @keydown.enter.prevent="postPlayerLimit"
    ></v-text-field>
    <v-container>
      <v-row justify="center">
        <v-col cols="auto">
          <AppButton @click.stop.prevent="postPlayerLimit"> Next </AppButton>
        </v-col>

        <v-col cols="auto">
          <AppButton
            class="btn text-4xl btn-primary wood-btn w-full max-w-[300px]"
            @click.stop.prevent="router.push('/')"
          >
            Cancel
          </AppButton>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<style scoped lang="scss"></style>
