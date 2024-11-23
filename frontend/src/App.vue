<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import {computed} from 'vue'
import {useGameStateChangeHandler} from "@/composables/gamestate-change-handler";

const route = useRoute()

useGameStateChangeHandler()

const bgClass = computed(() => route.meta?.bg)
</script>

<template>
  <div :class="bgClass" class="w-full h-full grid items-center">
    <RouterView />
  </div>
</template>

<style scoped></style>
