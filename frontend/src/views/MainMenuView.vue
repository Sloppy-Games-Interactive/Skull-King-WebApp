<script setup lang="ts">
import { inject, ref } from 'vue'
import { API_INJECTION_KEY, ApiService } from '@/core/rest/api'
import router from '@/core/router'
import { useLobbyStore } from '@/core/stores/lobbyStore'
import AppButton from '@/components/utils/AppButton.vue'
import { ButtonSize } from '@/components/utils/enums'
import { useGameStateStore } from '@/core/stores/gameState'
import { useChatStore } from '@/core/stores/chatStore'
import { getGitHubUrl } from '@/components/utils/getGithubUrl'

const lobby = useLobbyStore()
const gameState = useGameStateStore()
const chat = useChatStore()

gameState.clear()
chat.clear()

const api = inject(API_INJECTION_KEY) as ApiService

const newGame = async (event: MouseEvent) => {
  const uuid = await api.newGame()
  lobby.setLobbyUuid(uuid)
  await api.getStatus()

  await router.push('/new-game')
}

const loggedIn = ref(false)
const showMenu = ref(false)
</script>

<template>
  <div class="text-center grid gap-5 px-5 justify-center">
    <h1
      class="md:text-9xl sm:text-8xl text-7xl text-center text-white drop-shadow pb-10 title"
    >
      SkullKing
    </h1>

    <div
      class="grid gap-5 w-full max-w-[30rem] h-[100vh] max-h-[40vh] align-start auto-rows-min relative"
    >
      <template v-if="showMenu">
        <AppButton
          @click.stop.prevent="newGame"
          :size="ButtonSize.LARGE"
          key="new-game"
          >New Game</AppButton
        >

        <AppButton
          @click.stop.prevent="router.push('/join-lobby')"
          :size="ButtonSize.LARGE"
          key="join-game"
          >Join Game</AppButton
        >

        <AppButton
          @click.stop.prevent="router.push('/settings')"
          :size="ButtonSize.LARGE"
          key="settings"
          class="disabled"
          >Settings</AppButton
        >

        <div class="flex justify-end" v-if="!loggedIn" key="back">
          <AppButton
            @click.stop.prevent="showMenu = false"
            :size="ButtonSize.MEDIUM"
            icon-button
            ><fa-icon icon="chevron-left"
          /></AppButton>
        </div>
      </template>
      <template v-else>
        <AppButton
          tag="a"
          :href="getGitHubUrl('/')"
          :size="ButtonSize.LARGE"
          key="github"
          class="flex align-center gap-3"
          ><fa-icon :icon="['fab', 'github']" />Sign up with GitHub</AppButton
        >
        <AppButton
          @click.stop.prevent="showMenu = true"
          :size="ButtonSize.LARGE"
          key="guest"
          class="flex align-center gap-3"
          ><fa-icon :icon="['fas', 'user']" />Continue as Guest</AppButton
        >
      </template>
    </div>
  </div>
</template>

<style scoped lang="scss">
.title {
  font-family: var(--title-font-family), serif;
}
</style>
