<script setup lang="ts">
import StandardCard from '@/components/cards/StandardCard.vue'
import SpecialCard from '@/components/cards/SpecialCard.vue'
import { ref } from 'vue'
import PlayerStatusRow from "@/components/PlayerStatusRow.vue";
import PauseMenu from "@/components/PauseMenu.vue";
import ParallaxWrapper from '@/components/utils/ParallaxWrapper.vue'
import VPredictOverlay from "@/components/PredictOverlay.vue";

const cardType = ref('standard')
const standardCard = ref('blue')
const specialCard = ref('skullKing')
</script>

<template>
  <v-sheet class="mx-auto" width="300">
    <v-form>
      <v-text-field
        v-model="firstName"
        label="First name"
      ></v-text-field>
      <v-text-field
        v-model="lastName"
        label="Last name"
      ></v-text-field>
    </v-form>
  </v-sheet>
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
