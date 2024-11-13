<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'
import PauseMenu from "@/components/PauseMenu.vue";
import PlayerStatusRow from "@/components/PlayerStatusRow.vue";
import { useGameStateStore } from "@/stores/gameState";

const gameState = useGameStateStore();
</script>

<template>
  <div class="w-full h-full">
    <div class="grid grid-cols-3 md:grid-cols-6">
      <div class="col-span-3 m-4">
        <div v-for="player in gameState.players" :key="player.name" class="hidden sm:block">
          <!-- TODO: Change from player name id to numeric id (backend & frontend)-->
          <PlayerStatusRow class="m-9" :username="player.name" :score="player.score" profile-picture="https://media.istockphoto.com/id/816752606/photo/tv-test-card-or-test-pattern-generic.jpg?s=612x612&w=0&k=20&c=63Jcx_5bFnvBw9elRDLrLKjtDYXr70pKtUK0jXJ2_uY="/>
        </div>
      </div>
      <div class="mr-10 mt-10 ml-auto col-end-6 lg:col-end-7">
        <PauseMenu/>
      </div>
    </div>

    <div class="flex justify-center items-center">
<!--        Stack view of the cards-->
      <div class="h-[500px] w-[300px] md:h-[600px] md:w-[600px] flex justify-center items-center" ref="container">
        <div class="rotate-0 ">
          <StandardCard class="card" suit="blue" :value="4"></StandardCard>
        </div>
        <StandardCard suit="red" :value="9"></StandardCard>
        <StandardCard suit="yellow" :value="2"></StandardCard>
      </div>



<!--        own card holding-->



    </div>
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

.card {
  position: absolute;
  transform: scale(0.25);
  transform-origin: center;
}

select {
  color: black;
  width: auto;
}
</style>
