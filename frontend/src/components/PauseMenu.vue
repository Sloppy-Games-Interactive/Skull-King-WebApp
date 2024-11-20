<script setup lang="ts">
import { ref } from 'vue'

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}
</script>

<template>
  <button class="btn text-5xl btn-primary wood-btn" @click="openModal">
    Menu
  </button>

  <Teleport to="body">
    <Transition name="fade">
      <div v-if="isModalOpen" class="menu-backdrop" @click="closeModal"></div>
    </Transition>
  </Teleport>

  <Teleport to="body">
    <Transition name="slide">
      <div
        v-if="isModalOpen"
        class="menu h-full w-3/4 mx-auto mt-10 md:w-1/4 md:ml-auto md:mr-10 center"
        @click.stop
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

.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.3s ease,
    backdrop-filter 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  backdrop-filter: blur(0);
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  backdrop-filter: blur(20px);
}

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

.menu-backdrop {
  z-index: 999;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  backdrop-filter: blur(20px);
}

.menu {
  z-index: 1000;
  position: fixed;
  top: 0;
  right: 0;
}
</style>
