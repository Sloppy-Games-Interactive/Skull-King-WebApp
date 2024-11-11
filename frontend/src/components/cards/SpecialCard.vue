<script setup lang="ts">

import BaseCard from '@/components/cards/BaseCard.vue'
import { computed } from 'vue'

type Suit =
  | 'escape'
  | 'joker'
  | 'mermaid'
  | 'pirate'
  | 'skullKing'

const props = defineProps<{
  suit: Suit
}>()

const cardText = computed(() => {
  switch (props.suit) {
    case 'escape':
      return 'Escape'
    case 'joker':
      return 'Joker'
    case 'mermaid':
      return 'Mermaid'
    case 'pirate':
      return 'Pirate'
    case 'skullKing':
      return 'Skull King'
  }
})
</script>

<template>
  <BaseCard card-type="special" :suit="props.suit" :text="cardText"></BaseCard>
</template>

<style scoped lang="scss">

</style>
