import type { Serializable, SerializableJson } from '@/model/Serializable'
import { CardFactory, type CardInterface } from '@/model/Card'

export interface DeckInterface extends Serializable<DeckInterface> {
  cards: CardInterface[]
}

export class Deck implements DeckInterface {
  readonly cards: CardInterface[]

  constructor({ cards }: SerializableJson<DeckInterface>) {
    this.cards = cards.map(card => CardFactory.createCard(card))
  }

  toJSON(): SerializableJson<DeckInterface> {
    return {
      cards: this.cards,
    }
  }
}
