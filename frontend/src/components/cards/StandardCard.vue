<script setup lang="ts">
import BaseCard from '@/components/cards/BaseCard.vue'
import type {CardValueNumbers, StandardSuit} from '@/core/model/Card'

const props = defineProps<{
  suit: StandardSuit
  value: CardValueNumbers
}>()
</script>

<template>
  <BaseCard
    card-type="standard"
    :suit="props.suit"
    :value="props.value"
  ></BaseCard>
</template>

<style scoped lang="scss"></style>
