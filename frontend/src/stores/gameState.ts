import {defineStore} from "pinia";
import {computed, ref} from "vue";
import type {GameState} from "@/model/GameState";
import type {DeckInterface} from "@/model/Deck";
import type {TrickInterface} from "@/model/Trick";
import type {PlayerInterface} from "@/model/Player";

export const useGameStateStore = defineStore('gameState', () => {
  const round = ref(0);
  const phase = ref(''); // TODO phase enum
  const playerLimit = ref(0);
  const roundLimit = ref(0);
  const players = ref<PlayerInterface[]>([]);
  const deck = ref<DeckInterface|null>(null);
  const tricks = ref<TrickInterface[]>([]);

  const updateGameState = (state: GameState) => {
    round.value = state.round
    phase.value = state.phase
    playerLimit.value = state.playerLimit
    roundLimit.value = state.roundLimit
    players.value = state.players
    deck.value = state.deck
    tricks.value = state.tricks
  }

  const activePlayer = computed<PlayerInterface | undefined>(()  => {
    for (const player of players.value) {
      if (player.active) {
        return player
      }
    }

    return undefined;
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
    updateGameState
  }
})
