import type { Serializable, SerializableJson } from '@/model/Serializable'

export enum Suit {
  Red = '🟥',
  Yellow = '🟡',
  Blue = '🔷',
  Black = '☠️',
  Escape = '🏝️',
  SkullKing = '💀',
  Pirate = '🏴‍☠️',
  Mermaid = '🧜',
  Joker = '🃏',
}

export enum CardType {
  Standard,
  Trump,
  Special,
}

export const SuitProperties = {
  [Suit.Red]: { readable: '🟥', cardType: CardType.Standard },
  [Suit.Yellow]: { readable: '🟡', cardType: CardType.Standard },
  [Suit.Blue]: { readable: '🔷', cardType: CardType.Standard },
  [Suit.Black]: { readable: '☠️', cardType: CardType.Trump },
  [Suit.Escape]: { readable: '🏝️', cardType: CardType.Special },
  [Suit.SkullKing]: { readable: '💀', cardType: CardType.Special },
  [Suit.Pirate]: { readable: '🏴‍☠️', cardType: CardType.Special },
  [Suit.Mermaid]: { readable: '🧜', cardType: CardType.Special },
  [Suit.Joker]: { readable: '🃏', cardType: CardType.Special },
}

export interface CardInterface extends Serializable<CardInterface> {
  suit: Suit
  isSpecial: boolean
  isTrump: boolean
}

interface NumberRange {
  min: number
  max: number
}

type CardValueNumbers = number

export function assertValidCardValue(
  value: number,
): asserts value is CardValueNumbers {
  if (!isValidCardValue(value)) {
    throw new Error(`Invalid card value: ${value}`)
  }
}

export function isValidCardValue(value: number): value is CardValueNumbers {
  return isNumberInRange(value, { min: 1, max: 14 })
}

// Function to check if a number is within the specified range
function isNumberInRange(value: number, range: NumberRange): boolean {
  return value >= range.min && value <= range.max
}

export interface StandardCardInterface extends CardInterface {
  value: CardValueNumbers
}

export interface SpecialCardInterface extends CardInterface {}

export enum JokerBehaviour {
  Pirate = 'Pirate',
  Escape = 'Escape',
  None = 'None',
}

function getReadable(behaviour: JokerBehaviour): string {
  switch (behaviour) {
    case JokerBehaviour.Pirate:
      return SuitProperties[Suit.Pirate].readable
    case JokerBehaviour.Escape:
      return SuitProperties[Suit.Escape].readable
    case JokerBehaviour.None:
      return SuitProperties[Suit.Joker].readable
    default:
      return ''
  }
}

export interface JokerCardInterface extends SpecialCardInterface {
  as: JokerBehaviour
  playAs(behaviour: JokerBehaviour): JokerCardInterface
}

export abstract class Card implements CardInterface {
  readonly suit: Suit
  readonly isSpecial: boolean
  readonly isTrump: boolean

  constructor({ suit, isSpecial, isTrump }: SerializableJson<CardInterface>) {
    this.suit = suit
    this.isSpecial = isSpecial
    this.isTrump = isTrump
  }

  toJSON(): SerializableJson<CardInterface> {
    return {
      suit: this.suit,
      isSpecial: this.isSpecial,
      isTrump: this.isTrump,
    }
  }
}

export class StandardCard extends Card implements StandardCardInterface {
  readonly value: CardValueNumbers

  constructor({
    suit,
    value,
    isSpecial,
    isTrump,
  }: SerializableJson<StandardCardInterface>) {
    super({ suit, isSpecial, isTrump })

    this.value = value
  }

  toJSON(): SerializableJson<StandardCardInterface> {
    return {
      value: this.value,
      ...super.toJSON(),
    }
  }
}

export class SpecialCard extends Card implements SpecialCardInterface {}

type JokerCardRepresentation = Omit<
  SerializableJson<JokerCardInterface>,
  'playAs'
>

export class JokerCard extends Card implements JokerCardInterface {
  readonly as: JokerBehaviour

  constructor({ suit, as, isSpecial, isTrump }: JokerCardRepresentation) {
    super({ suit, isSpecial, isTrump })

    this.as = as
  }

  playAs(behaviour: JokerBehaviour): JokerCard {
    return new JokerCard({
      suit: this.suit,
      as: behaviour,
      isSpecial: this.isSpecial,
      isTrump: this.isTrump,
    })
  }

  toJSON(): JokerCardRepresentation {
    return {
      as: this.as,
      ...super.toJSON(),
    }
  }
}

export class CardFactory {
  static createCard(
    cardProps:
      | SerializableJson<CardInterface>
      | SerializableJson<StandardCardInterface>
      | SerializableJson<JokerCardRepresentation>,
  ): CardInterface {
    if (cardProps.isSpecial) {
      if (cardProps.suit === Suit.Joker) {
        return new JokerCard(cardProps as JokerCardRepresentation)
      }
      return new SpecialCard(cardProps)
    }
    return new StandardCard(
      cardProps as SerializableJson<StandardCardInterface>,
    )
  }
}
