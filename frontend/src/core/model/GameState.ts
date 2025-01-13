import { Player, type PlayerInterface } from '@/core/model/Player'
import type { Serializable, SerializableJson } from '@/core/model/Serializable'
import { Trick, type TrickInterface } from '@/core/model/Trick'
import { Deck, type DeckInterface } from '@/core/model/Deck'

// enum Phase {
//   case PrepareGame
// case PrepareTricks
// case PlayTricks
// case EndGame
// }

export enum Phase {
  None = '',
  PrepareGame = 'PrepareGame',
  PrepareTricks = 'PrepareTricks',
  PlayTricks = 'PlayTricks',
  EndGame = 'EndGame',
}

export interface GameStateInterface extends Serializable<GameStateInterface> {
  round: number
  phase: Phase
  playerLimit: number
  roundLimit: number
  players: PlayerInterface[]
  deck: DeckInterface
  tricks: TrickInterface[]
  lastTrickWinner: PlayerInterface | null
}

export class GameState implements GameStateInterface {
  readonly round
  readonly phase
  readonly playerLimit
  readonly roundLimit
  readonly players
  readonly deck
  readonly tricks
  readonly lastTrickWinner

  constructor({
    round,
    phase,
    playerLimit,
    roundLimit,
    players,
    deck,
    tricks,
    lastTrickWinner,
  }: SerializableJson<GameStateInterface>) {
    this.round = round
    this.phase = phase
    this.playerLimit = playerLimit
    this.roundLimit = roundLimit
    this.players = players.map(player => new Player(player))
    this.deck = new Deck(deck)
    this.tricks = tricks.map(trick => new Trick(trick))
    this.lastTrickWinner = lastTrickWinner ? new Player(lastTrickWinner) : null
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
      lastTrickWinner: this.lastTrickWinner,
    }
  }
}
