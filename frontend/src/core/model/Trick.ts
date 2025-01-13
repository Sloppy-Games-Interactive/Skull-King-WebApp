import { Player, type PlayerInterface } from '@/core/model/Player'
import { CardFactory, type CardInterface } from '@/core/model/Card'
import type { Serializable, SerializableJson } from '@/core/model/Serializable'

export interface TrickInterface extends Serializable<TrickInterface> {
  stack: TrickElementInterface[]
  winner: PlayerInterface | null
}

export interface TrickElementInterface
  extends Serializable<TrickElementInterface> {
  card: CardInterface
  player: PlayerInterface
}

export class TrickElement implements TrickElementInterface {
  readonly card: CardInterface
  readonly player: PlayerInterface

  constructor({ card, player }: SerializableJson<TrickElementInterface>) {
    this.card = CardFactory.createCard(card)
    this.player = new Player(player)
  }

  toJSON(): SerializableJson<TrickElementInterface> {
    return {
      card: this.card,
      player: this.player,
    }
  }
}

export class Trick implements TrickInterface {
  readonly stack: TrickElementInterface[]
  readonly winner: PlayerInterface | null

  constructor({ stack, winner }: SerializableJson<TrickInterface>) {
    this.stack = stack.map(element => new TrickElement(element))
    this.winner = winner ? new Player(winner) : null
  }

  toJSON(): SerializableJson<TrickInterface> {
    return {
      stack: this.stack,
      winner: this.winner,
    }
  }
}
