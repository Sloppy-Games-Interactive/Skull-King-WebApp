<script setup lang="ts">
import { ref } from 'vue'
import WoodButton from "@/components/WoodButton.vue";

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}

function closeModal() {
  isModalOpen.value = false
}
</script>

<template>
  <WoodButton text="Pause" :func="openModal" :use-router-link="false"/>

  <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
    <div class="h-full w-3/4 mx-auto mt-10 md:w-1/4 md:ml-auto md:mr-10 center" @click.stop>
      <div class="mx-auto">
        <h1 class="text-5xl text-center text-white">Pause Menu</h1>
        <div class="flex flex-col items-center mt-10">
          <WoodButton text="Resume" :func="closeModal" :use-router-link="false"/>
          <WoodButton text="Settings" url="/settings" :use-router-link="true"/>
          <WoodButton text="Main Menu" url="/" :use-router-link="true"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

.modal-overlay {
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
}

</style>
