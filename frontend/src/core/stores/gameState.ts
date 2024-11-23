import { defineStore } from 'pinia'
import {computed, nextTick, ref} from 'vue'
import { type GameStateInterface, Phase } from '@/core/model/GameState'
import type { DeckInterface } from '@/core/model/Deck'
import type { TrickInterface } from '@/core/model/Trick'
import type { PlayerInterface } from '@/core/model/Player'
import { useSessionStorage } from '@vueuse/core'

export const useGameStateStore = defineStore('gameState', () => {
  //TODO fix sessionstorage
  const sessionStorage = useSessionStorage<GameStateInterface | null>(
    'gameState',
    null,
    {
      deep: true,
    },
  )

  const currentGameState = ref<null | GameStateInterface>(null)

  const round = ref<number>(0)
  const phase = ref<Phase>(Phase.None)
  const playerLimit = ref<number>(0)
  const roundLimit = ref<number>(0)
  const players = ref<PlayerInterface[]>([])
  const deck = ref<DeckInterface | null>(null)
  const tricks = ref<TrickInterface[]>([])

  const updateGameState = (state: GameStateInterface) => {
    round.value = state.round
    phase.value = state.phase
    playerLimit.value = state.playerLimit
    roundLimit.value = state.roundLimit
    players.value = state.players
    deck.value = state.deck
    tricks.value = state.tricks

    // sessionStorage.value = state

    currentGameState.value = state
  }

  const activePlayer = computed<PlayerInterface | undefined>(() => {
    for (const player of players.value) {
      if (player.active) {
        return player
      }
    }

    return undefined
  })

  const currentTrick = computed<TrickInterface | undefined>(() => {
    return tricks.value[0] ?? undefined
  })

  return {
    round,
    phase,
    playerLimit,
    roundLimit,
    players,
    deck,
    tricks,
    activePlayer,
    currentTrick,
    updateGameState,
  }
})
