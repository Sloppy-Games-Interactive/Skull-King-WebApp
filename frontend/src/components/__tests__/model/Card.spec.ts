import { describe, it, expect } from 'vitest'
import {
  CardFactory,
  Suit,
  JokerBehaviour,
  StandardCard,
  SpecialCard,
  JokerCard,
  assertValidCardValue, isValidCardValue
} from '@/model/Card'

describe('CardFactory', () => {
  it('creates a StandardCard with valid properties', () => {
    const cardProps = { suit: Suit.Red, value: 5, isSpecial: false, isTrump: false }
    const card = CardFactory.createCard(cardProps)
    expect(card).toBeInstanceOf(StandardCard)
    expect(card.suit).toBe(Suit.Red)
    expect((card as StandardCard).value).toBe(5)
    expect(card.isSpecial).toBe(false)
    expect(card.isTrump).toBe(false)
  })

  it('creates a SpecialCard with valid properties', () => {
    const cardProps = { suit: Suit.Pirate, isSpecial: true, isTrump: false }
    const card = CardFactory.createCard(cardProps)
    expect(card).toBeInstanceOf(SpecialCard)
    expect(card.suit).toBe(Suit.Pirate)
    expect(card.isSpecial).toBe(true)
    expect(card.isTrump).toBe(false)
  })

  it('creates a JokerCard with valid properties', () => {
    const cardProps = { suit: Suit.Joker, as: JokerBehaviour.Pirate, isSpecial: true, isTrump: false }
    const card = CardFactory.createCard(cardProps)
    expect(card).toBeInstanceOf(JokerCard)
    expect(card.suit).toBe(Suit.Joker)
    expect((card as JokerCard).as).toBe(JokerBehaviour.Pirate)
    expect(card.isSpecial).toBe(true)
    expect(card.isTrump).toBe(false)
  })

  it('throws an error for invalid card value', () => {
    expect(() => assertValidCardValue(15)).toThrow('Invalid card value: 15')
  })

  it('validates a valid card value', () => {
    expect(isValidCardValue(10)).toBe(true)
  })

  it('invalidates an invalid card value', () => {
    expect(isValidCardValue(0)).toBe(false)
  })
})
