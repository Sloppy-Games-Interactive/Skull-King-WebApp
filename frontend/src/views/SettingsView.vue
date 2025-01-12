<script setup lang="ts">
import AppButton from '@/components/utils/AppButton.vue'
import router from '@/core/router'
</script>

<template>
  <div class="grid grid-cols-5 grid-flow-row auto-rows-max text-center gap-5">
    <AppButton @click.stop.prevent="router.push('/')">
      Go Back
    </AppButton>
  </div>
</template>

<style scoped lang="scss">
</style>
