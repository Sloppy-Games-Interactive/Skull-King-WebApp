<script setup lang="ts">
import BaseCard from '@/components/cards/BaseCard.vue'
import { computed } from 'vue'
import {type SpecialSuit, Suit} from "@/core/model/Card";

const props = defineProps<{
  suit: SpecialSuit
}>()

const cardText = computed(() => {
  switch (props.suit) {
    case Suit.Escape:
      return 'Escape'
    case Suit.Joker:
      return 'Joker'
    case Suit.Mermaid:
      return 'Mermaid'
    case Suit.Pirate:
      return 'Pirate'
    case Suit.SkullKing:
      return 'Skull King'
  }
})
</script>

<template>
  <BaseCard card-type="special" :suit="props.suit" :text="cardText"></BaseCard>
</template>

<style scoped lang="scss">

</style>
