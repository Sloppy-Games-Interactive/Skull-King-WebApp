import type { Serializable, SerializableJson } from '@/core/model/Serializable'
import { CardFactory, type CardInterface } from '@/core/model/Card'

export interface PlayerInterface extends Serializable<PlayerInterface> {
  id: string
  name: string
  profilePic: string
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
  readonly id: string
  readonly name: string
  readonly profilePic: string
  readonly hand: HandInterface
  readonly score: number
  readonly prediction: number | undefined
  readonly active: boolean

  constructor({
    id,
    name,
    hand,
    score,
    prediction,
    active,
    profilePic
  }: SerializableJson<PlayerInterface>) {
    this.id = id
    this.name = name
    this.profilePic = profilePic
    this.hand = new Hand(hand)
    this.score = score
    this.prediction = prediction
    this.active = active
  }

  toJSON(): SerializableJson<PlayerInterface> {
    return {
      id: this.id,
      name: this.name,
      profilePic: this.profilePic,
      hand: this.hand,
      score: this.score,
      prediction: this.prediction,
      active: this.active,
    }
  }
}
