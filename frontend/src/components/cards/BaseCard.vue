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
  <div class="card" :class="classes">
    <div v-if="typeof value === 'number'" class="values">
      <div class="top">
        <div class="value">{{ props.value }}</div>
      </div>
      <div class="bottom">
        <div class="value">{{ props.value }}</div>
      </div>
    </div>
    <div class="inner">
      <div v-if="typeof text === 'string'" class="text">{{ props.text }}</div>

      <div class="image"></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.card {
  padding: 30px;
  border-radius: 30px;
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

  .value {
    border: 20px solid var(--card-color);
    width: 200px;
    height: 200px;
    background: var(--accent-color);
    border-radius: 50%;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    pointer-events: none;

    box-shadow:
      inset 0px 0px 10px 0px rgb(0 0 0 / 15%),
      0px 0px 10px 0px rgb(0 0 0 / 15%);
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
    background-image: url('/images/cards/standard/octopusCard.jpeg');
  }
}

.red {
  --card-color: #557155;
  --accent-color: #4a4f37;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/ParrotCard.jpeg');
  }
}

.yellow {
  --card-color: #ffd014;
  --accent-color: #918468;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/treasureChestCard.jpeg');
  }
}

.trump {
  --card-color: #323943;
  --accent-color: #485260;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/standard/jollyRogerCard.jpeg');
  }
}

.escape {
  --card-color: whitesmoke;
  --accent-color: #5d4a3b;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/escapeCard.jpeg');
  }
}

.joker {
  --card-color: #774977;
  --accent-color: #494641;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/jokerCard.jpeg');
  }
}

.mermaid {
  --card-color: #97c7c2;
  --accent-color: #385d60;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/mermardeCard.jpeg');
  }
}

.pirate {
  --card-color: #d16a2d;
  --accent-color: #615e59;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/PirateCard.jpeg');
  }
}

.skullKing {
  --card-color: #692525;
  --accent-color: #433b35;

  background-color: var(--card-color);
  .image {
    background-image: url('/images/cards/special/skullkingCard.jpeg');
  }
}
</style>
