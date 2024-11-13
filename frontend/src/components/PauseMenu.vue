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
  <button class="btn text-5xl btn-primary wood-btn" @click="openModal">Menu</button>

  <transition name="slide">
    <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
      <div class="h-full w-3/4 mx-auto mt-10 md:w-1/4 md:ml-auto md:mr-10 center" @click.stop>
        <div class="mx-auto">
          <h1 class="text-5xl text-center text-white">Pause Menu</h1>
          <div class="flex flex-col items-center mt-10">
            <button class="btn text-5xl btn-primary wood-btn w-full max-w-[300px]" @click="closeModal">Resume</button>
            <router-link to="/" class="btn text-5xl btn-primary wood-btn w-full max-w-[300px] text-center">Main Menu</router-link>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<style scoped lang="scss">
@use 'src/assets/buttons';

.slide-enter-active, .slide-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease, backdrop-filter 0.3s ease;
}

.slide-enter-from, .slide-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

.slide-enter-to, .slide-leave-from {
  transform: translateX(0);
  opacity: 1;
}

.modal-overlay {
  z-index: 999;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(20px);

  &:before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
  }
}

</style>
