<script setup lang="ts">
import BaseCard from '@/components/cards/BaseCard.vue'

defineProps<{
  cardWidth?: number
  cardHeight?: number
}>()
</script>

<template>
  <BaseCard
    card-type="back"
    :card-width="cardWidth"
    :card-height="cardHeight"
    text="Skull King"
    subtext="Card Game"
  />
</template>

<style scoped lang="scss"></style>
