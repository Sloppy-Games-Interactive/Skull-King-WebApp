<script setup lang="ts">
import Modal from '@/components/utils/Modal.vue'
import { ref } from 'vue'
import { useGameStateStore } from '@/core/stores/gameState'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { FontAwesomeIcon as FaIcon } from '@fortawesome/vue-fontawesome'
import PlayerStatusRow from '@/components/PlayerStatusRow.vue'
import AppButton from '@/components/utils/AppButton.vue'
import { ButtonSize } from '@/components/utils/enums'
import { Phase } from '@/core/model/GameState'

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
  <div
    class="_grid-container md:grid md:gap-5 md:gap-y-7 flex-column align-start"
  >
    <div class="p-3 _menu hidden-sm-and-down">
      <AppButton :size="ButtonSize.SMALL" @click="openModal"
        ><fa-icon icon="users" class="text-2xl"
      /></AppButton>
    </div>

    <div
      class="_status-text flex align-center justify-center max-sm:w-full max-sm:h-full max-sm:leading-5 gap-3 p-3 text-white vertical-mid text-center text-3xl bg-white/40 rounded-br-lg md:rounded-bl-lg backdrop-blur-2xl"
      :class="{
        'bg-red-400/40': gameState.phase !== Phase.EndGame && !lobby.me?.active,
        'bg-green-400/40':
          gameState.phase !== Phase.EndGame && lobby.me?.active,
      }"
      @click="openModal"
    >
      <fa-icon icon="users" class="text-2xl" />

      <template v-if="gameState.phase === Phase.EndGame">Game is over</template>
      <template v-else-if="lobby.me?.active"> It's your turn! </template>
      <template v-else>
        {{ gameState.activePlayer?.name }} is playing a card.
      </template>
    </div>

    <div class="_me pl-3 hidden-sm-and-down" v-if="lobby.me">
      <PlayerStatusRow
        :player="lobby.me"
        :show-score="true"
        :show-status="true"
      />
    </div>
  </div>

  <Modal :open="isModalOpen" :on-click="closeModal"></Modal>

  <Teleport to="body">
    <Transition name="slide">
      <div
        v-if="isModalOpen"
        class="menu absolute top-0 left-0 z-[1000] h-full w-full flex justify-center"
        @click.stop="closeModal"
      >
        <div class="lg:w-2/5 w-full mx-auto pt-5">
          <div class="grid items-center mt-5 gap-5 px-3 place-items-center">
            <p class="text-center text-4xl text-white mb-2">
              Round {{ gameState.round }}
            </p>
            <template
              v-for="player of gameState.players"
              :key="
                gameState.tricks.filter(t => t.winner?.id === player.id)
                  .length + (player.prediction ?? 0)
              "
            >
              <PlayerStatusRow
                :player="player"
                :show-score="true"
                :show-status="true"
              />
            </template>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped lang="scss">
._grid-container {
  grid-template-areas:
    'menu status'
    'me me';
}

._menu {
  grid-area: menu;
}

._status-text {
  grid-area: status;
}

._me {
  grid-area: me;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateY(-100%);
}

.slide-enter-to,
.slide-leave-from {
  transform: translateY(0);
}
</style>
