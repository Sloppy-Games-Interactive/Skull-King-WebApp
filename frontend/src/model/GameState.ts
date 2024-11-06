import { Player, type PlayerInterface } from '@/model/Player'
import type { Serializable, SerializableJson } from '@/model/Serializable'
import { Trick, type TrickInterface } from '@/model/Trick'
import { Deck, type DeckInterface } from '@/model/Deck'

export interface GameStateInterface extends Serializable<GameStateInterface> {
  round: number
  phase: string
  playerLimit: number
  roundLimit: number
  players: PlayerInterface[]
  deck: DeckInterface
  tricks: TrickInterface[]
}

export class GameState implements GameStateInterface {
  readonly round
  readonly phase
  readonly playerLimit
  readonly roundLimit
  readonly players
  readonly deck
  readonly tricks

  constructor({
    round,
    phase,
    playerLimit,
    roundLimit,
    players,
    deck,
    tricks,
  }: SerializableJson<GameStateInterface>) {
    this.round = round
    this.phase = phase
    this.playerLimit = playerLimit
    this.roundLimit = roundLimit
    this.players = players.map(player => new Player(player))
    this.deck = new Deck(deck)
    this.tricks = tricks.map(trick => new Trick(trick))
  }

  toJSON(): SerializableJson<GameStateInterface> {
    return {
      round: this.round,
      phase: this.phase,
      playerLimit: this.playerLimit,
      roundLimit: this.roundLimit,
      players: this.players,
      deck: this.deck,
      tricks: this.tricks,
    }
  }
}
