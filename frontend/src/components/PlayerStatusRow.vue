<script setup lang="ts">
import type { PlayerInterface } from '@/core/model/Player'
import { useGameStateStore } from '@/core/stores/gameState'

const gameState = useGameStateStore()

defineProps<{
  player: PlayerInterface
  showScore?: boolean
  showStatus?: boolean
}>()
</script>

<template>
  <div
    class="_row sm:max-w-sm w-full bg-[#573324] rounded-l-[100px] rounded-r-[2rem] shadow"
    :class="{ score: showScore, status: showStatus }"
  >
    <div class="_img mr-4">
      <img
        :src="`/images/profiles/${player.profilePic}`"
        alt="Profile Picture"
        class="w-16 h-16 rounded-full border-2 border-amber-600 object-cover bg-[#573324] border-solid"
      />
    </div>

    <div class="_name grid align-center" :class="{'pr-4': !showScore}">
      <p class="text-3xl text-white text-start overflow-hidden grid">
        {{ player.name }}
      </p>
    </div>

    <div class="_status" v-if="showStatus">
      <p class="text-1xl text-white">
        Prediction: {{ player.prediction }} | Won tricks:
        {{ gameState.tricks.slice(1).filter(t => t.winner?.id === player.id).length }}
      </p>
    </div>

    <div class="_score grid p-1 justify-self-end ml-4" v-if="showScore">
      <p
        class="text-2xl text-white content-center text-center bg-[#3c2217] rounded-[1rem] p-3 place-self-center text-ellipsis overflow-hidden"
      >
        {{ player.score }}
      </p>
    </div>
  </div>
</template>

<style scoped lang="scss">
._row {
  display: grid;
  grid-template-areas:
    'img name'
    'img name';
  grid-template-columns: auto minmax(0, 1fr);

  &.score {
    grid-template-columns: auto 1fr auto;
    grid-template-areas: 'img name score';
  }

  &.status {
    grid-template-areas:
      'img name'
      'img status';
  }

  &.score.status {
    grid-template-areas:
      'img name score'
      'img status score';
  }
}

._img {
  grid-area: img;
}

._name {
  grid-area: name;
}

._score {
  grid-area: score;
}

._status {
  grid-area: status;
}
</style>
