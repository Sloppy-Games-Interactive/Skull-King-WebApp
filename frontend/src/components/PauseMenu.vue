<script setup lang="ts">
import { inject, ref } from 'vue'
import Modal from '@/components/utils/Modal.vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}

const api = inject(API_INJECTION_KEY) as ApiService
const fetchGameStateUpdate = async () => {
  await api.getStatus()
}
</script>

<template>
  <button class="btn text-5xl btn-primary wood-btn" @click="openModal">
    Menu
  </button>

  <Modal :open="isModalOpen" :on-click="closeModal"></Modal>

  <Teleport to="body">
    <Transition name="slide">
      <div
        v-if="isModalOpen"
        class="menu h-full w-full mx-auto mt-10 lg:w-1/4 md:ml-auto md:mr-10 center"
        @click.stop="closeModal"
      >
        <div class="mx-auto">
          <h1 class="text-5xl text-center text-white">Pause Menu</h1>
          <div class="flex flex-col items-center mt-10">
            <button
              class="btn text-5xl btn-primary wood-btn w-full max-w-[300px]"
              @click="closeModal"
            >
              Resume
            </button>

            <button
              class="btn text-5xl btn-primary wood-btn w-full max-w-[300px] disabled"
              @click.stop.prevent
            >
              Scores
            </button>
            <button
              class="btn text-5xl btn-primary wood-btn w-full max-w-[300px]"
              @click="fetchGameStateUpdate"
            >
              Update
            </button>
            <router-link
              to="/"
              class="btn text-5xl btn-primary wood-btn w-full max-w-[300px] text-center"
            >Main Menu</router-link
            >
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped lang="scss">
@use 'src/assets/buttons';

.slide-enter-active,
.slide-leave-active {
  transition:
    transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
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
