<script setup lang="ts">
import { onMounted, ref } from 'vue'
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
    hoverEffects: () => false,
  },
)

const containerRef = ref<HTMLElement | null>(null)
const isDown = ref(false)
const startX = ref(0)
const scrollLeft = ref(0)

onMounted(() => {
  if (containerRef.value) {
    containerRef.value.addEventListener('wheel', event => {
      event.preventDefault()
      containerRef.value!.scrollLeft += event.deltaY
    })

    containerRef.value.addEventListener('mousedown', event => {
      isDown.value = true
      startX.value = event.pageX - containerRef.value!.offsetLeft
      scrollLeft.value = containerRef.value!.scrollLeft
    })

    containerRef.value.addEventListener('mouseleave', () => {
      isDown.value = false
    })

    containerRef.value.addEventListener('mouseup', () => {
      isDown.value = false
    })

    containerRef.value.addEventListener('mousemove', event => {
      if (!isDown.value) return
      event.preventDefault()
      const x = event.pageX - containerRef.value!.offsetLeft
      const walk = (x - startX.value) * 2 // Adjust the scroll speed
      containerRef.value!.scrollLeft = scrollLeft.value - walk
    })
  }
})
</script>

<template>
  <div
    ref="containerRef"
    class="overflow-x-auto whitespace-nowrap text-center py-2 cursor-grab"
  >
    <template v-for="card in cards ?? []" :key="card.suit + card?.value">
      <Card
        class="inline-block m-3 rounded-3xl"
        :class="{
          'hover-effects': hoverEffects,
          clickable: typeof onClick === 'function',
        }"
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
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
  transform: none;
  box-shadow: none;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.5);
  }
}

.cursor-grab {
  cursor: grab;
}

.cursor-grab:active {
  cursor: grabbing;
}
</style>
