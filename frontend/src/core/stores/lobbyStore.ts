import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { PlayerInterface } from '@/core/model/Player'
import { useGameStateStore } from '@/core/stores/gameState'
import { useSessionStorage } from '@vueuse/core'

type LobbyStorage = {
  lobbyUuid: string | undefined,
  playerUuid: string | undefined,
}

export const useLobbyStore = defineStore('lobby', () => {
  const sessionStorage = useSessionStorage<LobbyStorage | null>(
    'lobby',
    null,
    {
      deep: true,
      serializer: {
        read: (v: string) => JSON.parse(v),
        write: (v: LobbyStorage | null) => JSON.stringify(v),
      }
    },
  )

  const lobbyUuid = ref<string | undefined>(sessionStorage.value?.lobbyUuid ?? undefined);
  const playerUuid = ref<string | undefined>(sessionStorage.value?.playerUuid ?? undefined);

  const setLobbyUuid = (uuid: string | undefined) => {
    lobbyUuid.value = uuid;

    sessionStorage.value = {
      lobbyUuid: uuid,
      playerUuid: playerUuid.value,
    }
  }

  const setPlayerUuid = (uuid: string | undefined) => {
    playerUuid.value = uuid;

    sessionStorage.value = {
      lobbyUuid: lobbyUuid.value,
      playerUuid: uuid,
    }
  }

  const gameState = useGameStateStore()
  const me = computed<PlayerInterface | undefined>(() => {
    if (!playerUuid.value) {
      return undefined;
    }

    for (const player of gameState.players) {
      if (player.id === playerUuid.value) {
        return player
      }
    }

    return undefined
  })

  const hostPlayer = computed<PlayerInterface | undefined>(() => {
    return gameState.players[0] ?? undefined
  })

  const isHost = computed<boolean>(() => {
    return (typeof me.value !== 'undefined') && (typeof hostPlayer.value !== 'undefined') && (me.value.id === hostPlayer.value.id)
  })

  return {
    lobbyUuid,
    setLobbyUuid,
    playerUuid,
    setPlayerUuid,
    hostPlayer,
    isHost,
    me
  }
})
