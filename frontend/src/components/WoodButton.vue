<script setup lang="ts">

const props = defineProps<{
  text: string
  url?: string
  func?: () => void
  useRouterLink?: boolean
}>()

const url = props.url ?? '#'
</script>

<template>

    <router-link :to="url" class="btn text-5xl btn-primary wood-btn" v-if="props.useRouterLink">
      {{ props.text }}
    </router-link>

    <a @click="props.func" class="btn text-5xl btn-primary wood-btn" v-else>
      {{ props.text }}
    </a>

</template>

<style scoped lang="scss">
@use '../assets/buttons';

.btn {
  width: 100%;
  max-width: 400px;
}
</style>
