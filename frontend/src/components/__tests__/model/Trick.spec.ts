import { describe, it, expect } from 'vitest'
import {Trick, TrickElement} from '@/model/Trick'
import type {TrickInterface} from '@/model/Trick'
import { Suit, StandardCard } from '@/model/Card'
import type { SerializableJson } from '@/model/Serializable'
import { Player } from '@/model/Player'

describe('Trick', () => {
  it('creates a Trick with valid properties', () => {
    const playerProps = { name: 'Player1', hand: [], score: 0, prediction: 0, active: false,
      toJSON() {
        return { name: 'Player1', hand: [], score: 0, prediction: 0, active: false }
      }
    }
    const cardProps = { suit: Suit.Red, value: 5, isSpecial: false, isTrump: false,
      toJSON() {
        return { suit: Suit.Red, value: 5, isSpecial: false, isTrump: false }
      }
    }
    const trickElementProps = {
      card: cardProps,
      player: playerProps,
      toJSON: () => ({
        card: cardProps.toJSON(),
        player: playerProps.toJSON()
      })
    }
    const trickProps: SerializableJson<TrickInterface> = { stack: [trickElementProps] }
    const trick = new Trick(trickProps)
    expect(trick.stack[0]).toBeInstanceOf(TrickElement)
    expect(trick.stack[0].card).toBeInstanceOf(StandardCard)
    expect(trick.stack[0].player).toBeInstanceOf(Player)
  })

  it('serializes a Trick to JSON correctly', () => {
    const playerProps = { name: 'Player1', hand: [], score: 0, prediction: 0, active: false }
    const cardProps = { suit: Suit.Red, value: 5, isSpecial: false, isTrump: false }
    const trickElementProps = { card: cardProps, player: playerProps }
    const trickProps: SerializableJson<TrickInterface> = { stack: [trickElementProps] }
    const trick = new Trick(trickProps)
    const json = trick.toJSON()
    expect(json.stack[0].card.suit).toBe(Suit.Red)
    expect((json.stack[0].card as StandardCard).value).toBe(5)
    expect(json.stack[0].player.name).toBe('Player1')
  })

  it('handles an empty Trick correctly', () => {
    const trickProps: SerializableJson<TrickInterface> = { stack: [] }
    const trick = new Trick(trickProps)
    expect(trick.stack.length).toBe(0)
  })
})
