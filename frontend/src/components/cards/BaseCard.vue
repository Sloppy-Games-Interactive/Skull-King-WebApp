<script setup lang="ts">
import { computed, ref } from 'vue'
import {
  useElementSize,
  useParentElement,
  useResizeObserver,
} from '@vueuse/core'
import useResizeCard from '@/components/cards/resize-card'
import { Suit } from '@/model/Card'

type CardType = 'standard' | 'special'

type CardValue = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13

const props = defineProps<{
  cardType: CardType
  suit: Suit
  value?: CardValue
  text?: string
  flipped?: boolean
}>()

const suitClass = computed(() => {
  switch (props.suit) {
    case Suit.Black:
      return 'trump'
    case Suit.Blue:
      return 'blue'
    case Suit.Red:
      return 'red'
    case Suit.Yellow:
      return 'yellow'
    case Suit.Joker:
      return 'joker'
    case Suit.Escape:
      return 'escape'
    case Suit.Mermaid:
      return 'mermaid'
    case Suit.Pirate:
      return 'pirate'
    case Suit.SkullKing:
      return 'skullKing'
  }
})

const classes = computed(
  () =>
    props.cardType + ' ' + suitClass.value + ' ' + (props.flipped ? 'flipped' : ''),
)

const $parent = useParentElement()

const { height, width } = useElementSize($parent)
const availableHeight = ref(height)
const availableWidth = ref(width)

useResizeObserver($parent, entries => {
  const entry = entries[0]
  const { width, height } = entry.contentRect
  availableHeight.value = height
  availableWidth.value = width
})

const { style } = useResizeCard(availableWidth, availableHeight)
</script>

<template>
  <div class="card textured" :style="style" :class="classes">
    <div v-if="typeof value === 'number'" class="values top">
      <div class="value textured">{{ props.value }}</div>
    </div>
    <div class="inner textured light">
      <div v-if="typeof text === 'string'" class="text">{{ props.text }}</div>

      <div class="image"></div>
    </div>
    <div v-if="typeof value === 'number'" class="values bottom">
      <div class="value textured">{{ props.value }}</div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.textured {
  &:before {
    border-radius: var(--border-radius);
    content: '';
    background-color: transparent;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 600 600'%3E%3Cfilter id='a'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23a)'/%3E%3C/svg%3E");
    background-repeat: repeat;
    background-size: var(--textured_background-size);
    top: 0;
    left: 0;
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 1;
    opacity: 0.2;

    mask-image: radial-gradient(circle, transparent 0%, black 50%);
  }
  &.light {
    &:before {
      opacity: 0.1;
    }
  }
}

.card {
  --border-radius: var(--card_border-radius);

  padding: var(--card_padding);
  border-radius: var(--border-radius);
  max-width: var(--card_max-width);
  width: 100%;
  max-height: var(--card_max-height);
  height: 100%;
  position: relative;

  background: red;
  font-family: var(--title-font-family), serif;
}

.values {
  position: relative;
  z-index: 10;

  color: white;
  font-size: var(--values_font-size);

  --x-outset: var(--values_x-outset);
  --y-outset: var(--values_y-outset);
  --border-radius: 50%;
}

.value {
  border: var(--value_border-width) solid var(--card-color);
  width: var(--value_width);
  height: var(--value_height);
  background: var(--accent-color);
  border-radius: var(--border-radius);
  display: inline-flex;
  justify-content: center;
  align-items: center;
  user-select: none;
  position: relative;
  overflow: hidden;

  box-shadow:
    inset 0 0 var(--value_box-shadow-size) 0 rgb(0 0 0 / 25%),
    0 0 var(--value_box-shadow-size) 0 rgb(0 0 0 / 15%);
  text-shadow: var(--value_text-shadow-offset) var(--value_text-shadow-offset)
    var(--value_text-shadow-size) rgb(0 0 0 / 60%);
}

.top {
  position: absolute;
  top: var(--y-outset);
  left: var(--x-outset);
}

.bottom {
  position: absolute;
  bottom: var(--y-outset);
  right: var(--x-outset);

  .value {
    transform: rotate(180deg);
  }
}

.inner {
  border-radius: var(--inner_border-radius);
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;

  z-index: 1;
}

.text {
  position: absolute;
  bottom: 0;
  padding-top: var(--text_padding-top);
  padding-bottom: var(--text_padding-bottom);
  font-size: var(--text_font-size);
  width: 100%;
  text-align: center;
  color: white;
  background: linear-gradient(0deg, black, transparent);
  user-select: none;
}

.image {
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;

  box-shadow: inset 0 0 var(--image_box-shadow-size) 0 rgb(0 0 0 / 50%);
}

.blue {
  --card-color: #456791;
  --accent-color: #283949;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/octopus.jpeg');
  }
}

.red {
  --card-color: #557155;
  --accent-color: #4a4f37;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/parrot.jpeg');
  }
}

.yellow {
  --card-color: #918468;
  --accent-color: #5f543c;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/treasure_chest.jpeg');
  }
}

.trump {
  --card-color: #323943;
  --accent-color: #485260;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/jolly_roger.jpeg');
  }
}

.escape {
  --card-color: whitesmoke;
  --accent-color: #5d4a3b;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/escape.jpeg');
  }
}

.joker {
  --card-color: #774977;
  --accent-color: #494641;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/joker.jpeg');
  }
}

.mermaid {
  --card-color: #7bbfb8;
  --accent-color: #385d60;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/mermaid.jpeg');
  }
}

.pirate {
  --card-color: #d16a2d;
  --accent-color: #615e59;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/pirate.jpeg');
  }
}

.skullKing {
  --card-color: #692525;
  --accent-color: #433b35;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/skull_king.jpeg');
  }
}
</style>
