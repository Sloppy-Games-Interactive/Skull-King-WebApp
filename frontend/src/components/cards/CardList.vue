<script setup lang="ts">
import { type CardInterface, CardSize } from '@/core/model/Card'
import VCard from '@/components/cards/Card.vue'

defineOptions({
  name: 'VCardList',
})

withDefaults(
  defineProps<{
    cards: CardInterface[]
    onClick?: (card: CardInterface) => {}
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
  <div class="overflow-x-auto whitespace-nowrap">
    <template v-for="card in cards ?? []">
      <VCard
        class="inline-block mx-3"
        :card="card"
        :size="cardSize"
        @click="onClick(card)"
      ></VCard>
    </template>
  </div>
</template>

<style scoped lang="scss"></style>
