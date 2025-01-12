<script setup lang="ts">
import { ref } from 'vue'
import Modal from '@/components/utils/Modal.vue'
import AppButton from '@/components/utils/AppButton.vue'
import router from '@/core/router'
import { FontAwesomeIcon as FaIcon } from '@fortawesome/vue-fontawesome'
import { ButtonSize } from '@/components/utils/enums'

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}

function leave() {
  router.push('/')
}
</script>

<template>
  <AppButton :size="ButtonSize.SMALL" @click="openModal">
    <fa-icon icon="bars" />
  </AppButton>

  <Modal :open="isModalOpen" :on-click="closeModal"></Modal>

  <Teleport to="body">
    <Transition name="slide">
      <div
        v-if="isModalOpen"
        class="menu h-full w-full mx-auto mt-10 lg:w-1/4 md:ml-auto md:mr-10 center"
        @click.stop="closeModal"
      >
        <div class="mx-auto">
          <div class="grid items-center mt-10 gap-5 px-3">
            <AppButton @click="closeModal"> Resume </AppButton>

            <AppButton class="disabled" @click.stop.prevent> Scores </AppButton>

            <AppButton @click.stop.prevent="leave"> Main Menu </AppButton>
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
  transition: transform 0.3s ease;
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
