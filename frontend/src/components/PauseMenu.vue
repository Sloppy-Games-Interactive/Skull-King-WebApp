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
  <button @click="openModal" class="pause-button">Pause</button>

  <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h2>Pause Menu</h2>
      <button @click="closeModal">Close</button>
      <!-- Add more pause menu options here -->
    </div>
  </div>
</template>

<style scoped lang="scss">
.pause-button {
  /* Style your pause button here */
}

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

.modal-content {
  background: saddlebrown;
  padding: 2rem;
  border-radius: 8px;

  /* Style your modal content here */
}
</style>
