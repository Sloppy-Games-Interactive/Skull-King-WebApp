import {describe, it, expect, vi} from 'vitest'
import { ApiService } from '@/rest/api'
import { GameState } from '@/model/GameState'
import type { CardInterface } from '@/model/Card'

describe('ApiService', () => {
  it('fetches game status successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as vi

    const apiService = new ApiService()
    const gameState = await apiService.getStatus()
    expect(gameState).toBeInstanceOf(GameState)
    expect(gameState.round).toBe(1)
  })

  it('creates a new game successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.newGame()
    expect(gameState).toBeInstanceOf(GameState)
    expect(gameState.round).toBe(1)
  })

  it('sets player limit successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.setPlayerLimit(4)
    expect(gameState).toBeInstanceOf(GameState)
    expect(gameState.playerLimit).toBe(4)
  })

  it('sets player name successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [{ id: 1, name: 'Player1' }], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.setPlayerName('Player1')
    expect(gameState).toBeInstanceOf(GameState)
    expect(gameState.players[0].name).toBe('Player1')
  })

  it('sets prediction successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.setPrediction(3)
    expect(gameState).toBeInstanceOf(GameState)
  })

  it('plays a card successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const card: CardInterface = { suit: 'Red', value: 5, isSpecial: false, isTrump: false }
    const gameState = await apiService.playCard(card)
    expect(gameState).toBeInstanceOf(GameState)
  })

  it('saves the game successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.saveGame()
    expect(gameState).toBeInstanceOf(GameState)
  })

  it('loads the game successfully', async () => {
    const mockResponse = { round: 1, phase: 'start', playerLimit: 4, roundLimit: 10, players: [], deck: { cards: [] }, tricks: [] }
    global.fetch = vi.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockResponse),
      })
    ) as jest.Mock

    const apiService = new ApiService()
    const gameState = await apiService.loadGame()
    expect(gameState).toBeInstanceOf(GameState)
  })
})
