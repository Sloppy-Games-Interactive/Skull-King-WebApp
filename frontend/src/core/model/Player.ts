import type { Serializable, SerializableJson } from '@/core/model/Serializable'
import { CardFactory, type CardInterface } from '@/core/model/Card'
import { v4 as uuid } from 'uuid';


export interface PlayerInterface extends Serializable<PlayerInterface> {
  id: number
  name: string
  profilePicUrl: string
  uuid: typeof uuid
  hand: HandInterface
  score: number
  prediction: number | undefined
  active: boolean
}

export interface HandInterface extends Serializable<HandInterface> {
  cards: CardInterface[]
}

export class Hand implements HandInterface {
  readonly cards: CardInterface[]

  constructor({ cards }: SerializableJson<HandInterface>) {
    this.cards = cards.map(card => CardFactory.createCard(card))
  }

  toJSON(): SerializableJson<HandInterface> {
    return {
      cards: this.cards,
    }
  }
}

export class Player implements PlayerInterface {
  readonly id: number
  readonly name: string
  readonly profilePicUrl: string
  readonly hand: HandInterface
  readonly uuid: typeof uuid
  readonly score: number
  readonly prediction: number | undefined
  readonly active: boolean

  constructor({
    id,
    name,
    uuid,
    hand,
    score,
    prediction,
    active,
  }: SerializableJson<PlayerInterface>) {
    this.id = id
    this.name = name
    this.profilePicUrl = `https://api.dicebear.com/9.x/bottts/png`
    this.uuid = uuid
    this.hand = new Hand(hand)
    this.score = score
    this.prediction = prediction
    this.active = active
  }

  toJSON(): SerializableJson<PlayerInterface> {
    return {
      id: this.id,
      name: this.name,
      profilePicUrl: this.profilePicUrl,
      uuid: this.uuid,
      hand: this.hand,
      score: this.score,
      prediction: this.prediction,
      active: this.active,
    }
  }
}
