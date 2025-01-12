<script setup lang="ts">

import { ref } from 'vue'

import PlayerLobbyList from '@/components/PlayerLobbyList.vue'

</script>

<template>
  <PlayerLobbyList player-img="" player-name="test"></PlayerLobbyList>
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
