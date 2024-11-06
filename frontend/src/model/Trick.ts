import { Player, type PlayerInterface } from '@/model/Player'
import { CardFactory, type CardInterface } from '@/model/Card'
import type { Serializable, SerializableJson } from '@/model/Serializable'

export interface TrickInterface extends Serializable<TrickInterface> {
  stack: TrickElementInterface[]
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

  constructor({ stack }: SerializableJson<TrickInterface>) {
    this.stack = stack.map(element => new TrickElement(element))
  }

  toJSON(): SerializableJson<TrickInterface> {
    return {
      stack: this.stack,
    }
  }
}
