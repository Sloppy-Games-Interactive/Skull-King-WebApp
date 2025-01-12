<script setup lang="ts">

import { ref } from 'vue'

import PlayerLobbyList from '@/components/PlayerLobbyList.vue'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'

</script>

<template>
  <PlayerStatusRow username="username" profile-picture="https://api.dicebear.com/9.x/bottts/png" />
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
