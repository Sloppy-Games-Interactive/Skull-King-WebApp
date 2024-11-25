<script setup lang="ts">
import { type CardInterface, CardSize } from '@/core/model/Card'
import Card from '@/components/cards/Card.vue'

withDefaults(
  defineProps<{
    cards: CardInterface[]
    onClick?: (card: CardInterface) => void
    cardSize?: CardSize
  }>(),
  {
    cards: () => [],
    onClick: () => (c: CardInterface) => {},
    cardSize: () => CardSize.medium,
  },
)
</script>

<template>
  <div class="overflow-x-auto whitespace-nowrap text-center">
    <template v-for="card in cards ?? []">
      <Card
        class="inline-block m-3"
        :card="card"
        :size="cardSize"
        @click="onClick(card)"
      ></Card>
    </template>
  </div>
</template>

<style scoped lang="scss"></style>
