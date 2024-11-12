<script setup lang="ts">

const props = defineProps<{
  username: string,
  profilePicture: string,
  score?: number,
}>()

</script>

<template>
  <div class="flex items-center p-3 shadow rounded-lg max-w-sm border-2 border-amber-600 ml-7 bg-[#573324]">
    <img
      :src="props.profilePicture"
      alt="Profile Picture"
      class="w-24 h-24 rounded-full -ml-10 border-2 border-amber-600 absolute object-cover"
    />
    <div class="ml-16 flex w-full">
      <p class="text-4xl font-semibold text-white content-center">{{ props.username }}</p>
      <p v-if="props.score !== undefined" class="text-xl font-semibold text-white ml-auto content-center text-center mr-4 bg-[#3c2217] border-amber-600 border-2 rounded-full w-12 h-12">{{ props.score }}</p>    </div>
  </div>
</template>

<style scoped lang="scss">

</style>
