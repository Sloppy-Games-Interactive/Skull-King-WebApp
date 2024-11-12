<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'
import ParallaxWrapper from '@/components/ParallaxWrapper.vue'
import PauseMenu from "@/components/PauseMenu.vue";
import BaseCard from "@/components/cards/BaseCard.vue";

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')
</script>

<template>
  <div class="w-full h-full">
    <div class="flex fixed w-full">
      <div class="mr-10 mt-10 ml-auto p-0">
        <PauseMenu/>
      </div>
    </div>

    <div class="flex justify-center items-center h-full">

    </div>
  </div>
</template>

<style scoped lang="scss">
select {
  color: black;
  width: auto;
}
</style>
