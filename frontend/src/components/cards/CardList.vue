<script setup lang="ts">
import { type CardInterface, CardSize } from '@/core/model/Card'
import Card from '@/components/cards/Card.vue'

withDefaults(
  defineProps<{
    cards: CardInterface[]
    onClick?: (card: CardInterface) => void
    cardSize?: CardSize
    hoverEffects?: boolean
  }>(),
  {
    cards: () => [],
    cardSize: () => CardSize.medium,
    hoverEffects: () => false
  },
)
</script>

<template>
  <div class="overflow-x-auto whitespace-nowrap text-center py-2">
    <template v-for="card in cards ?? []">
      <Card
        class="inline-block m-3 rounded-3xl"
        :class="{'hover-effects': hoverEffects, 'clickable': typeof onClick === 'function'}"
        :card="card"
        :size="cardSize"
        @click="() => onClick && onClick(card)"
      ></Card>
    </template>
  </div>
</template>

<style scoped lang="scss">
.clickable {
  cursor: pointer;
}

.hover-effects {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  transform: none;
  box-shadow: none;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.5);
  }
}
</style>
