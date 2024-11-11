<script setup lang="ts">
import { computed } from 'vue'

type CardType = 'standard' | 'special'
type Suit =
  | 'blue'
  | 'red'
  | 'yellow'
  | 'trump'
  | 'escape'
  | 'joker'
  | 'mermaid'
  | 'pirate'
  | 'skullKing'

type CardValue = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13

const props = defineProps<{
  cardType: CardType
  suit: Suit
  value?: CardValue
  text?: string
  flipped?: boolean
}>()

const classes = computed(
  () =>
    props.cardType + ' ' + props.suit + ' ' + (props.flipped ? 'flipped' : ''),
)
</script>

<template>
  <div class="card textured" :class="classes">
    <div v-if="typeof value === 'number'" class="values">
      <div class="top">
        <div class="value textured">{{ props.value }}</div>
      </div>
      <div class="bottom">
        <div class="value textured">{{ props.value }}</div>
      </div>
    </div>
    <div class="inner textured light">
      <div v-if="typeof text === 'string'" class="text">{{ props.text }}</div>

      <div class="image"></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.textured {
  &:before {
    border-radius: var(--border-radius);
    content: "";
    background-color: transparent;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 600 600'%3E%3Cfilter id='a'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23a)'/%3E%3C/svg%3E");
    background-repeat: repeat;
    background-size: 182px;
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
  --border-radius: 30px;

  padding: 30px;
  border-radius: var(--border-radius);
  width: 747px;
  height: 1122px;
  position: relative;

  background: red;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  font-family: var(--title-font-family), serif;
}

.values {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  z-index: 10;

  color: white;
  font-size: 9rem;

  --x-outset: -38px;
  --y-outset: -18px;
  --border-radius: 50%;

  .value {
    border: 20px solid var(--card-color);
    width: 200px;
    height: 200px;
    background: var(--accent-color);
    border-radius: var(--border-radius);
    display: inline-flex;
    justify-content: center;
    align-items: center;
    pointer-events: none;
    position: relative;
    overflow: hidden;

    box-shadow:
      inset 0px 0px 10px 0px rgb(0 0 0 / 25%),
      0px 0px 10px 0px rgb(0 0 0 / 15%);
    text-shadow: 1px 1px 5px rgb(0 0 0 / 60%);
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
}

.inner {
  border-radius: 20px;
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;

  z-index: 1;
}

.text {
  position: absolute;
  bottom: 0;
  padding-top: 2rem;
  padding-bottom: 2rem;
  font-size: 7rem;
  width: 100%;
  text-align: center;
  color: white;
  background: linear-gradient(0deg, black, transparent);
}

.image {
  background-position: center center;
  background-size: cover;
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;

  box-shadow: inset 0px 0px 20px 0px rgb(0 0 0 / 50%);
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
