import { GameState } from '@/core/model/GameState'
import type { CardInterface } from '@/core/model/Card'
import type {InjectionKey} from "vue";
import { v4 as uuid } from 'uuid';
import { getCookieValue } from "@/core/utils/Cookies";
import { Logger } from "tslog";

abstract class BaseApiService {
  logger = new Logger({ name: "SkullKingLogger" });

  private readonly baseUrl: string

  protected constructor(baseUrl: string) {
    this.baseUrl = baseUrl
  }

  async get(path: string) {
    // TODO implement GET parameters
    const response = await fetch(this.baseUrl + path, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
    return await response.json()
  }

  async post(path: string, data: object | undefined = undefined) {
    const response = await fetch(this.baseUrl + path, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: typeof data === 'undefined' ? '' : JSON.stringify(data),
      // for cookies sent with the request
      //credentials: 'include',
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

export const API_INJECTION_KEY = Symbol() as InjectionKey<ApiService>;

const lobbyUuid = getCookieValue('lobbyUuid') ?? ""
const playerUuid = getCookieValue('playerId') ?? ""

export class ApiService extends BaseApiService {
  constructor() {
    super('http://localhost:9000')
  }

  async getStatus(lobbyUuid: typeof uuid): Promise<GameState> {
    const state = await this.post('/status', { lobbyUuid: lobbyUuid.toString() })
    return new GameState(state)
  }

  async newGame(): Promise<typeof uuid> {
    this.logger.debug('Creating new game')
    const lobbyUuid = await this.post('/new-lobby')
    this.logger.debug('New game created: ', lobbyUuid)
    return lobbyUuid.uuid
  }

  async setPlayerLimit(limit: number): Promise<GameState> {
    const state = await this.post('/set-player-limit', { lobbyUuid: lobbyUuid.toString(), limit: limit })
    return new GameState(state);
  }

  async setPlayerName(name: string): Promise<GameState> {
    const state = await this.post('/join-lobby', { lobbyUuid: lobbyUuid.toString(), playerUuid: playerUuid.toString(), name: name })
    return new GameState(state);
  }

  async joinLobby(lobbyUuid: typeof uuid, name: string): Promise<GameState> {
    const state = await this.post('/join-lobby', { lobbyUuid: lobbyUuid.toString(), playerUuid: playerUuid.toString(), name: name })
    return new GameState(state);
  }

  async setPrediction(prediction: number): Promise<GameState> {
    const state = await this.post('/set-prediction', { lobbyUuid: lobbyUuid.toString(), playerUuid: playerUuid.toString(), prediction: prediction })
    return new GameState(state);
  }

  async playCard(card: CardInterface): Promise<GameState> {
    const state = await this.post('/play-card', { lobbyUuid: lobbyUuid.toString(), playerUuid: playerUuid.toString(), card: card })
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
