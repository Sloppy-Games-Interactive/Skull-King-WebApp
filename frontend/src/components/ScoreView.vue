<script setup lang="ts">

import Modal from '@/components/utils/Modal.vue'
import { ref } from 'vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { FontAwesomeIcon as FaIcon } from '@fortawesome/vue-fontawesome'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import { getEnv } from '@/core/utils/EnvLoader'
import { FRONTEND_URL } from '@/core/utils/Constants'


const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}

const gameState = useGameStateStore()
const lobby = useLobbyStore()

</script>

<template>
  <div class="w-full h-full flex align-center justify-between p-3 text-white vertical-mid text-center text-3xl bg-white/40 rounded-br-lg backdrop-blur-2xl" @click.stop.prevent="openModal">
    <fa-icon icon="users" class="text-2xl"/>

    <template v-if="lobby.me?.active">
      It's your turn!
    </template>
    <template v-else>
      {{ gameState.activePlayer?.name }} is playing a card.
    </template>
  </div>

  <Modal :open="isModalOpen" :on-click="closeModal">
    <Transition name="slide">
      <div
        v-if="isModalOpen"
        class="menu h-full w-full mx-auto mt-5 lg:w-1/4 md:ml-auto md:mr-10 center"
        @click.stop="closeModal"
      >
        <div class="mx-auto">
          <div class="grid items-center mt-5 gap-5 px-3">
            <p class="text-center text-4xl text-white mb-2">Round {{ gameState.round }}</p>
            <template v-for="(player, idx) of gameState.players" :key="(player.prediction ?? '') + gameState.tricks.filter(t => t.winner?.id === player.id).length">
              <PlayerStatusRow :username="player.name" :profile-picture="`${FRONTEND_URL}/images/profiles/${idx + 1}.png`" :score="player.score" />
              <p class="text-2xl text-white mt-n5 mb-2 ml-2 text-center">Prediction: {{player.prediction}} | Won tricks: {{gameState.tricks.filter(t => t.winner?.id === player.id).length}}</p>
            </template>
          </div>
        </div>
      </div>
    </Transition>
  </Modal>
</template>


<style scoped lang="scss">

</style>
