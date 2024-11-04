<script setup lang="ts">
const loadGame = (event: MouseEvent) => {
  console.log('loadGame');
}
</script>

<template>
  <div class="grid grid-cols-5 grid-flow-row auto-rows-max text-center gap-5">
    <h1 class="row-auto col-span-full text-9xl text-center text-white drop-shadow pb-10 title">SkullKing</h1>

    <router-link to="/new-game" class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn">New Game</router-link>

    <a @click.stop.prevent="loadGame" class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn disabled">Load Game</a>

    <router-link to="/settings" class="col-span-1 col-start-3 btn text-5xl btn-primary wood-btn">Settings</router-link>
  </div>
</template>

<style scoped lang="scss">
@use "../assets/buttons";

.title {
  font-size: 10rem;
  font-family: var(--title-font-family), serif;
}
</style>
