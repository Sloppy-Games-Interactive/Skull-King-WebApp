<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import {computed, ref} from 'vue'
import PauseMenu from "@/components/PauseMenu.vue";
import PlayerStatusRow from "@/components/PlayerStatusRow.vue";
import { useGameStateStore } from "@/stores/gameState";

const gameState = useGameStateStore();

const getRandomRotationAngle = () => {
  const min = -8; // minimum rotation angle
  const max = 8;  // maximum rotation angle
  return Math.random() * (max - min) + min;
};
</script>

<template>
  <div class="w-full h-full">
    <div class="grid grid-cols-3 md:grid-cols-6">
      <div class="col-span-3 m-4">
        <div v-for="player in gameState.players" :key="player.name" class="hidden sm:block">
          <PlayerStatusRow class="m-9" :username="player.name" :score="player.score" profile-picture="https://media.istockphoto.com/id/816752606/photo/tv-test-card-or-test-pattern-generic.jpg?s=612x612&w=0&k=20&c=63Jcx_5bFnvBw9elRDLrLKjtDYXr70pKtUK0jXJ2_uY="/>
        </div>
      </div>
      <div class="mr-10 mt-10 ml-auto col-end-6 lg:col-end-7">
        <PauseMenu/>
      </div>
    </div>

    <div class="flex justify-center items-center">
      <div class="relative h-[500px] w-[300px] md:h-[600px] md:w-[600px] flex justify-center items-center" ref="container">
        <div class="absolute bg-red-500 w-52 h-72 rounded-md" :style="{ transform: `rotate(${getRandomRotationAngle()}deg)` }"/>
        <div class="absolute bg-blue-500 w-52 h-72 rounded-md" :style="{ transform: `rotate(${getRandomRotationAngle()}deg)` }"/>
        <div class="absolute bg-yellow-500 w-52 h-72 rounded-md" :style="{ transform: `rotate(${getRandomRotationAngle()}deg)` }"/>
        <div v-for="card in gameState.deck" :key="card.toString()" class="absolute bg-green-500 w-52 h-72 rounded-md" :style="{ transform: `rotate(${getRandomRotationAngle()}deg)` }"/>
      </div>
    </div>

<!--    hand cards-->
<div class="flex justify-center -space-x-16">
  <div class="bg-yellow-500 w-52 h-72 rounded-md hover:transform hover:-translate-y-4 transition-transform duration-300"></div>
  <div class="bg-blue-500 w-52 h-72 rounded-md hover:transform hover:-translate-y-4 transition-transform duration-300"></div>
  <div class="bg-red-500 w-52 h-72 rounded-md hover:transform hover:-translate-y-4 transition-transform duration-300"></div>
  <div class="bg-green-500 w-52 h-72 rounded-md hover:transform hover:-translate-y-4 transition-transform duration-300"></div>
</div>


  </div>
</template>

<style scoped lang="scss">
select {
  color: black;
  width: auto;
}
</style>
