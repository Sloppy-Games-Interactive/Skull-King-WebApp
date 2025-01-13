<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  open: boolean
  title: string
  message: string
  onClose: () => void
}>()

const snackbar = ref(false)

watch(() => props.open, () => {
  snackbar.value = props.open
})
</script>

<template>
  <div class="text-center">
    <v-snackbar color="error" vertical :v-model="snackbar">
      <div class="text-subtitle-1 pb-2">{{props.title}}</div>

      <p>{{props.message}}</p>

      <template v-slot:actions>
        <v-btn
          color="indigo"
          variant="text"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<style scoped lang="scss">

</style>
