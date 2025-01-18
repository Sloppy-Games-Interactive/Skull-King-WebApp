import { defineStore } from 'pinia'
import { computed, reactive, ref } from 'vue'
import { GameState, type GameStateInterface, Phase } from '@/core/model/GameState'
import type { DeckInterface } from '@/core/model/Deck'
import type { TrickInterface } from '@/core/model/Trick'
import type { PlayerInterface } from '@/core/model/Player'
import { useSessionStorage } from '@vueuse/core'

export const useGameStateStore = defineStore('gameState', () => {
  const sessionStorage = useSessionStorage<GameStateInterface | null>(
    'gameState',
    null,
    {
      deep: true,
      serializer: {
        read: (v: string) => new GameState(JSON.parse(v)),
        write: (v: GameStateInterface | null) => JSON.stringify(v),
      }
    },
  )

  const round = ref<number>(sessionStorage.value?.round ?? 0)
  const phase = ref<Phase>(sessionStorage.value?.phase ?? Phase.None)
  const playerLimit = ref<number>(sessionStorage.value?.playerLimit ?? 0)
  const roundLimit = ref<number>(sessionStorage.value?.roundLimit ?? 0)
  const players = reactive<PlayerInterface[]>(sessionStorage.value?.players ?? [])
  const deck = ref<DeckInterface | null>(sessionStorage.value?.deck ?? null)
  const tricks = reactive<TrickInterface[]>(sessionStorage.value?.tricks ?? [])
  const lastTrickWinner = ref<PlayerInterface | null>(sessionStorage.value?.lastTrickWinner ?? null)

  const updateGameState = (state: GameStateInterface) => {
    round.value = state.round
    phase.value = state.phase
    playerLimit.value = state.playerLimit
    roundLimit.value = state.roundLimit
    players.splice(0, players.length, ...state.players)
    deck.value = state.deck
    tricks.splice(0, tricks.length, ...state.tricks)
    lastTrickWinner.value = state.lastTrickWinner

    sessionStorage.value = state
  }

  const activePlayer = computed<PlayerInterface | undefined>(() => {
    for (const player of players) {
      if (player.active) {
        return player
      }
    }

    return undefined
  })

  const currentTrick = computed<TrickInterface | undefined>(() => {
    return tricks[0] ?? undefined
  })

  const clear = () => {
    sessionStorage.value = null

    round.value = 0
    phase.value = Phase.None
    playerLimit.value = 0
    roundLimit.value = 0
    players.splice(0, players.length)
    deck.value = null
    tricks.splice(0, tricks.length)
    lastTrickWinner.value = null
  }

  return {
    round,
    phase,
    playerLimit,
    roundLimit,
    players,
    deck,
    tricks,
    lastTrickWinner,
    activePlayer,
    currentTrick,
    updateGameState,
    clear,
  }
})
