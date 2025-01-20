<script setup lang="ts">
import LoginBoxView from '@/components/LoginBoxView.vue'

</script>

<template>
  <div style="display: flex">
    <LoginBoxView />
  </div>
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
