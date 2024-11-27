<script setup lang="ts">
import PlayerStatusRow from "@/components/PlayerStatusRow.vue";
import Modal from "@/components/utils/Modal.vue";
import {inject, ref} from "vue";
import {useGameStateStore} from "@/core/stores/gameState";
import {API_INJECTION_KEY, ApiService} from "@/core/rest/api";

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}

const gameState = useGameStateStore()
</script>

<template>
  <button class="btn text-5xl btn-primary wood-btn" @click="openModal">
    >
  </button>

<!--  <Modal :open="isModalOpen" :on-click="closeModal"></Modal>-->

  <Teleport to="body">
    <Transition name="slide">
      <!--TODO: fix widths & other styling-->
      <div
        v-if="isModalOpen"
        class="menu h-full w-full mx-auto mt-10 lg:w-1/4 md:ml-auto md:mr-10 center"
        @click.stop="closeModal"
      >
        <div
          v-for="player in gameState.players"
          :key="player.name"
          class="hidden sm:block"
        >
          <PlayerStatusRow
            class="m-9"
            :username="player.name"
            :score="player.score"
            profile-picture="https://media.istockphoto.com/id/816752606/photo/tv-test-card-or-test-pattern-generic.jpg?s=612x612&w=0&k=20&c=63Jcx_5bFnvBw9elRDLrLKjtDYXr70pKtUK0jXJ2_uY="
          />
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped lang="scss">
.slide-enter-active,
.slide-leave-active {
  transition:
    transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(-100%);
}

.slide-enter-to,
.slide-leave-from {
  transform: translateX(0);
}

.menu {
  z-index: 1000;
  position: fixed;
  top: 0;
  right: 0;
}
</style>
