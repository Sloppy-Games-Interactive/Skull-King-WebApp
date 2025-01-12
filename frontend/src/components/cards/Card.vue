<script setup lang="ts">
import {
  type SpecialSuit,
  type StandardCard as StandardCardType,
  type SpecialCard as SpecialCardType,
  type StandardSuit,
  CardSize,
} from '@/core/model/Card'
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from "@/components/cards/SpecialCard.vue";
import { computed } from 'vue'

const props = defineProps<{
  card: StandardCardType | SpecialCardType
  size?: CardSize
}>()

// + 0.025 to prevent y-overflow
const aspectRatio = 1122 / 747 + 0.025;

const dimensions = computed(() => {
  switch (props.size) {
    case CardSize.mini:
      return {
        width: '80px',
        height: `${aspectRatio * 80}px`,
      }
    case CardSize.small:
      return {
        width: '176px',
        height: `${aspectRatio * 176}px`,
      }
    case CardSize.medium:
      return {
        width: '221px',
        height: `${aspectRatio * 221}px`,
      }
    case CardSize.large:
      return {
        width: '265px',
        height: `${aspectRatio * 265}px`,
      }
    case CardSize.xlarge:
      return {
        width: '399px',
        height: `${aspectRatio * 399}px`,
      }
    default:
      return {
        width: '747px',
        height: '1122px',
      }
  }
})

const style = computed(() => {
  return {
    width: dimensions.value.width,
    height: dimensions.value.height
  }
})
</script>

<template>
  <div :style="style">
    <SpecialCard v-if="props.card.isSpecial || (props.card as StandardCardType).value === 0" :suit="props.card.suit as SpecialSuit" />
    <StandardCard
      v-else
      :suit="props.card.suit as StandardSuit"
      :value="(props.card as StandardCardType).value"
    />
  </div>
</template>

<style scoped lang="scss"></style>
