import { GameState } from '@/core/model/GameState'
import type { CardInterface } from '@/core/model/Card'
import type {InjectionKey} from "vue";
import { v4 as uuid } from 'uuid';
import { getCookieValue } from "@/core/utils/Cookies";
import { Logger } from "tslog";
import { useLobbyStore } from '@/core/stores/lobbyStore'
import type { Store } from 'pinia'

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

    if (!response.ok) {
      // error happened
      // todo: handle errors
    }

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

    if (!response.ok) {
      // error happened
      // todo: handle errors
    }

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

    if (!response.ok) {
      // error happened
      // todo: handle errors
    }

    return await response.json()
  }

  async delete(path: string) {
    const response = await fetch(this.baseUrl + path, {
      method: 'DELETE',
    })

    if (!response.ok) {
      // error happened
      // todo: handle errors
    }

    return await response.json()
  }
}

export const API_INJECTION_KEY = Symbol() as InjectionKey<ApiService>;

export class ApiService extends BaseApiService {
  constructor(
    private lobbyStore: Store | undefined
  ) {
    // TODO: get url from env variable or something
    super( 'http://localhost:9000')
  }

  get lobbyUuid(): string | undefined {
    if (!this.lobbyStore) {
      this.lobbyStore = useLobbyStore();
    }

    return this.lobbyStore.lobbyUuid
    // return getCookieValue('lobbyUuid') ?? undefined
  }

  get playerUuid(): string | undefined {
    return getCookieValue('playerId') ?? undefined
  }

  async getStatus(lobbyUuid: typeof uuid): Promise<GameState> {
    const state = await this.post('/status', { lobbyUuid })
    return new GameState(state)
  }

  async newGame(): Promise<typeof uuid> {
    this.logger.debug('Creating new game')
    const response = await this.post('/new-lobby')
    this.logger.debug('New game created: ', response)
    return response.uuid
  }

  async setPlayerLimit(limit: number): Promise<GameState> {
    const state = await this.post('/set-player-limit', { lobbyUuid: this.lobbyUuid, limit: limit })
    return new GameState(state);
  }

  async setPlayerName(name: string): Promise<GameState> {
    const state = await this.post('/join-lobby', { lobbyUuid: this.lobbyUuid, playerUuid: this.playerUuid, name: name })
    return new GameState(state);
  }

  async joinLobby(lobbyUuid: string, name: string): Promise<GameState> {
    const state = await this.post('/join-lobby', { lobbyUuid, playerUuid: this.playerUuid, name: name })
    return new GameState(state);
  }

  async setPrediction(prediction: number): Promise<GameState> {
    const state = await this.post('/set-prediction', { lobbyUuid: this.lobbyUuid, playerUuid: this.playerUuid, prediction: prediction })
    return new GameState(state);
  }

  async playCard(card: CardInterface): Promise<GameState> {
    const state = await this.post('/play-card', { lobbyUuid: this.lobbyUuid, playerUuid: this.playerUuid, card: card })
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
