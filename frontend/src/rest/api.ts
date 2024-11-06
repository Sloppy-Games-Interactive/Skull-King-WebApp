import { GameState } from '@/model/GameState'
import type { CardInterface } from '@/model/Card'

abstract class BaseApiService {
  private readonly baseUrl: string

  protected constructor(baseUrl: string) {
    this.baseUrl = baseUrl
  }

  async get(path: string) {
    // TODO implement GET parameters
    const response = await fetch(this.baseUrl + path)
    return await response.json()
  }

  async post(path: string, data: object | undefined = undefined) {
    const response = await fetch(this.baseUrl + path, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: typeof data === 'undefined' ? '' : JSON.stringify(data),
    })
    return await response.json()
  }

  async put(path: string, data: object | undefined = undefined) {
    const response = await fetch(this.baseUrl + path, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: typeof data === 'undefined' ? '' : JSON.stringify(data),
    })
    return await response.json()
  }

  async delete(path: string) {
    const response = await fetch(this.baseUrl + path, {
      method: 'DELETE',
    })
    return await response.json()
  }
}

export class ApiService extends BaseApiService {
  constructor() {
    super('localhost:9000')
  }

  async getStatus(): Promise<GameState> {
    const state = await this.get('/status')
    return new GameState(state)
  }

  async newGame(): Promise<GameState> {
    const state = await this.post('/new-game')
    return new GameState(state)
  }

  async setPlayerLimit(limit: number): Promise<GameState> {
    const state = await this.post('/set-player-limit', { limit })
    return new GameState(state);
  }

  async setPlayerName(name: string): Promise<GameState> {
    const state = await this.post('/set-player-name', { name })
    return new GameState(state);
  }

  async setPrediction(prediction: number): Promise<GameState> {
    const state = await this.post('/set-prediction', { prediction })
    return new GameState(state);
  }

  async playCard(card: CardInterface): Promise<GameState> {
    const state = await this.post('/play-card', { card })
    return new GameState(state);
  }

  async saveGame(): Promise<GameState> {
    const state = await this.post('/save-game')
    return new GameState(state);
  }

  async loadGame(loadState: GameState | undefined = undefined): Promise<GameState> {
    const state = await this.post('/load-game', loadState)
    return new GameState(state);
  }
}
