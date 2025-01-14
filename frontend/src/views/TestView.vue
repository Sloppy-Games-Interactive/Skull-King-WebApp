<script setup lang="ts">
import Card from '@/components/cards/Card.vue'
import { CardSize, Suit } from '@/core/model/Card'
import {SpecialCard} from '@/core/model/Card'

const c = new SpecialCard({suit: Suit.Pirate})
</script>

<template>
  <div style="display: flex">
    <Card :card="null" :size="CardSize.medium"></Card>
    <Card :card="c" :size="CardSize.medium"></Card>
  </div>
</template>

<style scoped lang="scss">
.card-container {
  width: 600px;
  height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

select {
  color: black;
  width: auto;
}
</style>
