<script setup lang="ts">

import BaseCard from '@/components/cards/BaseCard.vue'

type Suit =
  | 'blue'
  | 'red'
  | 'yellow'
  | 'trump'
type CardValue = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13

const props = defineProps<{
  suit: Suit
  value: CardValue
}>()
</script>

<template>
<BaseCard card-type="standard" :suit="props.suit" :value="props.value"></BaseCard>
</template>

<style scoped lang="scss">

</style>
