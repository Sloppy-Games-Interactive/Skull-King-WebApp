import { describe, it, expect } from 'vitest'
import {GameState} from '@/core/model/GameState'
import type {GameStateInterface} from '@/core/model/GameState'
import { Suit, StandardCard, SpecialCard, JokerCard, JokerBehaviour } from '@/core/model/Card'
import { Deck } from '@/core/model/Deck'
import { Trick } from '@/core/model/Trick'
import {Player} from "@/core/model/Player";
import type {SerializableJson} from "@/core/model/Serializable";

describe('GameState', () => {
  it('creates a GameState with valid properties', () => {
    const playerProps = {
      id: 1,
      name: 'Player1',
      score: 0,
      prediction: 0,
      active: false,
      hand: [],
      toJSON: () => ({ id: 1, name: 'Player1', hand: [], score: 0, prediction: 0, active: false })
    }
    const cardProps = {
      suit: Suit.Red,
      value: 5,
      isSpecial: false,
      isTrump: false,
      toJSON: () => ({ suit: Suit.Red, value: 5, isSpecial: false, isTrump: false })
    }
    const deckProps = {
      cards: [cardProps],
      toJSON: () => ({ cards: [cardProps.toJSON()] })
    }
    const trickProps = {
      cards: [cardProps],
      stack: [],
      toJSON: () => ({ cards: [cardProps.toJSON()], stack: [] })
    }
    const gameStateProps: SerializableJson<GameStateInterface> = {
      round: 1,
      phase: 'start',
      playerLimit: 4,
      roundLimit: 10,
      players: [playerProps],
      deck: deckProps,
      tricks: [trickProps],
    }

    const gameState = new GameState(gameStateProps)
    expect(gameState.round).toBe(1)
    expect(gameState.phase).toBe('start')
    expect(gameState.playerLimit).toBe(4)
    expect(gameState.roundLimit).toBe(10)
    expect(gameState.players[0]).toBeInstanceOf(Player)
    expect(gameState.deck).toBeInstanceOf(Deck)
    expect(gameState.tricks[0]).toBeInstanceOf(Trick)
  })

  it('serializes a GameState to JSON correctly', () => {
    const playerProps = {
      id: 1,
      name: 'Player1',
      score: 0,
      prediction: 0,
      active: false,
      hand: [],
      toJSON: () => ({ id: 1, name: 'Player1', hand: [], score: 0, prediction: 0, active: false })
    }
    const cardProps = {
      suit: Suit.Red,
      value: 5,
      isSpecial: false,
      isTrump: false,
      toJSON: () => ({ suit: Suit.Red, value: 5, isSpecial: false, isTrump: false })
    }
    const deckProps = {
      cards: [cardProps],
      toJSON: () => ({ cards: [cardProps.toJSON()] })
    }
    const trickProps = {
      cards: [cardProps],
      stack: [],
      toJSON: () => ({ cards: [cardProps.toJSON()], stack: [] })
    }
    const gameStateProps: SerializableJson<GameStateInterface> = {
      round: 1,
      phase: 'start',
      playerLimit: 4,
      roundLimit: 10,
      players: [playerProps],
      deck: deckProps,
      tricks: [trickProps]
    }
    const gameState = new GameState(gameStateProps)
    const json = gameState.toJSON()
    expect(json.round).toBe(1)
    expect(json.phase).toBe('start')
    expect(json.playerLimit).toBe(4)
    expect(json.roundLimit).toBe(10)
    expect((json.players[0] as Player).id).toBe(1)
    expect((json.players[0] as Player).name).toBe('Player1')
    expect(json.deck.cards[0].suit).toBe(Suit.Red)
    expect(json.deck.cards[0].value).toBe(5)
    expect((json.tricks[0] as Trick).cards[0].suit).toBe(Suit.Red)
    expect((json.tricks[0] as Trick).cards[0].value).toBe(5)
  })

  it('handles an empty GameState correctly', () => {
    const gameStateProps: SerializableJson<GameStateInterface> = {
      round: 0,
      phase: '',
      playerLimit: 0,
      roundLimit: 0,
      players: [],
      deck: { cards: [], toJSON: () => ({ cards: [] }) },
      tricks: []
    }
    const gameState = new GameState(gameStateProps)
    expect(gameState.round).toBe(0)
    expect(gameState.phase).toBe('')
    expect(gameState.playerLimit).toBe(0)
    expect(gameState.roundLimit).toBe(0)
    expect(gameState.players.length).toBe(0)
    expect(gameState.deck.cards.length).toBe(0)
    expect(gameState.tricks.length).toBe(0)
  })
})
