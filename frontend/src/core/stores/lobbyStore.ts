import { defineStore } from 'pinia'
import {ref} from 'vue'

export const useLobbyStore = defineStore('lobby', () => {
  const lobbyUuid = ref<string | undefined>(undefined);
  const playerUuid = ref<string | undefined>(undefined);

  const setLobbyUuid = (uuid: string | undefined) => {
    lobbyUuid.value = uuid;
  }

  const setPlayerUuid = (uuid: string | undefined) => {
    playerUuid.value = uuid;
  }

  return {
    lobbyUuid,
    setLobbyUuid,
    playerUuid,
    setPlayerUuid,
  }
})
