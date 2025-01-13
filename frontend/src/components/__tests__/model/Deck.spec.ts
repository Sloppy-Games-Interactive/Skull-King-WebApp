import { describe, it, expect } from 'vitest'
import {Deck} from '@/core/model/Deck'
import type {DeckInterface} from '@/core/model/Deck'
import {CardFactory, Suit, StandardCard, SpecialCard, JokerCard, JokerBehaviour, type CardInterface} from '@/core/model/Card'
import type {SerializableJson} from "@/core/model/Serializable";

describe('Deck', () => {
  it('creates a Deck with valid StandardCard properties', () => {
    const cardProps = {
      suit: Suit.Red,
      value: 5,
      isSpecial: false,
      isTrump: false,
      // Why is this necessary for new Deck()?
      toJSON: () => ({ suit: Suit.Red, value: 5, isSpecial: false, isTrump: false })
    }
    const deckProps: SerializableJson<DeckInterface> = { cards: [cardProps] }
    const deck = new Deck(deckProps)
    expect(deck.cards[0]).toBeInstanceOf(StandardCard)
    expect(deck.cards[0].suit).toBe(Suit.Red)
    expect((deck.cards[0] as StandardCard).value).toBe(5)
    expect(deck.cards[0].isSpecial).toBe(false)
    expect(deck.cards[0].isTrump).toBe(false)
  })

  it('creates a Deck with valid SpecialCard properties', () => {
    const cardProps = { suit: Suit.Pirate, isSpecial: true, isTrump: false,
      toJSON: () => ({ suit: Suit.Pirate, isSpecial: true, isTrump: false }) }
    const deckProps = { cards: [cardProps] }
    const deck = new Deck(deckProps)

    expect(deck.cards[0]).toBeInstanceOf(SpecialCard)
    expect(deck.cards[0].suit).toBe(Suit.Pirate)
    expect(deck.cards[0].isSpecial).toBe(true)
    expect(deck.cards[0].isTrump).toBe(false)
  })

  it('creates a Deck with valid JokerCard properties', () => {
    const cardProps = { suit: Suit.Joker, as: JokerBehaviour.Pirate, isSpecial: true, isTrump: false,
      toJSON: () => ({ suit: Suit.Joker, as: JokerBehaviour.Pirate, isSpecial: true, isTrump: false }) }
    const deckProps = { cards: [cardProps] }
    const deck = new Deck(deckProps)

    expect(deck.cards[0]).toBeInstanceOf(JokerCard)
    expect(deck.cards[0].suit).toBe(Suit.Joker)
    expect(deck.cards[0].isSpecial).toBe(true)
    expect(deck.cards[0].isTrump).toBe(false)
  })

  it('serializes a Deck to JSON correctly', () => {
    const cardProps = { suit: Suit.Red, value: 5, isSpecial: false, isTrump: false,
      toJSON: () => ({ suit: Suit.Red, value: 5, isSpecial: false, isTrump: false }) }
    const deckProps = { cards: [cardProps] }
    const deck = new Deck(deckProps)
    const json = deck.toJSON()
    expect(json.cards[0].suit).toBe(Suit.Red)
    expect((json.cards[0] as StandardCard).value).toBe(5)
    expect(json.cards[0].isSpecial).toBe(false)
    expect(json.cards[0].isTrump).toBe(false)
  })

  it('handles an empty Deck correctly', () => {
    const deckProps = { cards: [] }
    const deck = new Deck(deckProps)
    expect(deck.cards.length).toBe(0)
  })
})
