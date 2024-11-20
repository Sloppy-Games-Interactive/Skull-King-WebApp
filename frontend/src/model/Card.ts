import type { Serializable, SerializableJson } from '@/model/Serializable'

export enum Suit {
  Red = 'Red',
  Yellow = 'Yellow',
  Blue = 'Blue',
  Black = 'Black',
  Escape = 'Escape',
  SkullKing = 'SkullKing',
  Pirate = 'Pirate',
  Mermaid = 'Mermaid',
  Joker = 'Joker',
}

export type StandardSuit = Suit.Red | Suit.Blue | Suit.Black | Suit.Yellow
export type SpecialSuit =
  | Suit.Escape
  | Suit.Joker
  | Suit.Mermaid
  | Suit.Pirate
  | Suit.SkullKing

export const STANDARD_SUITS: StandardSuit[] = [
  Suit.Red,
  Suit.Blue,
  Suit.Black,
  Suit.Yellow,
]
export const SPECIAL_SUITS: SpecialSuit[] = [
  Suit.Escape,
  Suit.Joker,
  Suit.Mermaid,
  Suit.Pirate,
  Suit.SkullKing,
]

export enum CardSize {
  mini = 'mini',
  small = 'small',
  medium = 'medium',
  large = 'large',
  xlarge = 'xlarge',
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

export type CardValueNumbers =
  | 1
  | 2
  | 3
  | 4
  | 5
  | 6
  | 7
  | 8
  | 9
  | 10
  | 11
  | 12
  | 13

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

  constructor({ suit, value }: SerializableJson<StandardCardInterface>) {
    super({ suit, isSpecial: false, isTrump: suit === Suit.Black })

    this.value = value
  }

  toJSON(): SerializableJson<StandardCardInterface> {
    return {
      value: this.value,
      ...super.toJSON(),
    }
  }
}

export class SpecialCard extends Card implements SpecialCardInterface {
  constructor({ suit }: SerializableJson<SpecialCardInterface>) {
    super({ suit, isSpecial: true, isTrump: false })
  }
}

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
  static createCard(cardProps: SerializableJson<CardInterface>): CardInterface {
    if (SPECIAL_SUITS.includes(cardProps.suit)) {
      return cardProps.suit === Suit.Joker
        ? new JokerCard(cardProps as JokerCardRepresentation)
        : new SpecialCard(cardProps)
    }
    return new StandardCard(
      cardProps as SerializableJson<StandardCardInterface>,
    )
  }
}
