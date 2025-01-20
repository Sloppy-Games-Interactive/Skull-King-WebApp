import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { PlayerInterface } from '@/core/model/Player'
import { useGameStateStore } from '@/core/stores/gameState'
import { useSessionStorage } from '@vueuse/core'

type LobbyStorage = {
  lobbyUuid: string | undefined
  playerUuid: string | undefined
}

export const useLobbyStore = defineStore('lobby', () => {
  const sessionStorage = useSessionStorage<LobbyStorage | null>('lobby', null, {
    deep: true,
    serializer: {
      read: (v: string) => JSON.parse(v),
      write: (v: LobbyStorage | null) => JSON.stringify(v),
    },
  })

  const gameState = useGameStateStore()

  const lobbyUuid = ref<string | undefined>(
    sessionStorage.value?.lobbyUuid ?? undefined,
  )
  const playerUuid = ref<string | undefined>(
    sessionStorage.value?.playerUuid ?? undefined,
  )

  const setLobbyUuid = (uuid: string | undefined) => {
    lobbyUuid.value = uuid

    sessionStorage.value = {
      lobbyUuid: uuid,
      playerUuid: playerUuid.value,
    }
  }

  const setPlayerUuid = (uuid: string | undefined) => {
    playerUuid.value = uuid

    sessionStorage.value = {
      lobbyUuid: lobbyUuid.value,
      playerUuid: uuid,
    }
  }

  const hostPlayer = computed<PlayerInterface | undefined>(() => {
    return gameState.players[0] ?? undefined
  })

  const me = computed<PlayerInterface | undefined>(() => {
    return gameState.players.find(player => player.id === playerUuid.value)
  })

  const isHost = computed<boolean>(() => {
    return (
      gameState.players[0]?.id === playerUuid.value &&
      typeof playerUuid.value !== 'undefined'
    )
  })

  const clear = () => {
    sessionStorage.value = null

    lobbyUuid.value = undefined
    playerUuid.value = undefined
  }

  return {
    lobbyUuid,
    setLobbyUuid,
    playerUuid,
    setPlayerUuid,
    hostPlayer,
    isHost,
    me,
    clear,
  }
})
