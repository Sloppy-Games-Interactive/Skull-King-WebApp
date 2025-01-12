import type { CardInterface } from '@/core/model/Card'
import type {InjectionKey} from "vue";
import { Logger } from "tslog";
import { useLobbyStore } from '@/core/stores/lobbyStore'
import { useEventBus } from '@vueuse/core'
import { ErrorBus, ErrorEvent, ErrorEventName, GameStateBus, GameStateEvent, GameStateEventName } from '@/core/event-bus'

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
      return Promise.reject(response.body);
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
      const body = response.body;
      if (body) {
        return Promise.reject(await response.text());
      }
      return Promise.reject('No response body');
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
      return Promise.reject(response.body);
    }

    return await response.json()
  }

  async delete(path: string) {
    const response = await fetch(this.baseUrl + path, {
      method: 'DELETE',
    })

    if (!response.ok) {
      return Promise.reject(response.body);
    }

    return await response.json()
  }
}

export const API_INJECTION_KEY = Symbol() as InjectionKey<ApiService>;

type UseLobbyStoreType = ReturnType<typeof useLobbyStore>

export class ApiService extends BaseApiService {
  constructor(
    private lobby: UseLobbyStoreType | undefined,
    private gameStateBus = useEventBus<GameStateEvent>(GameStateBus),
    private errorBus = useEventBus<ErrorEvent>(ErrorBus)
  ) {
    // TODO: get url from env variable or something
    super('http://localhost:9000')
  }

  private handleError(e: unknown) {
    // handle different error cases
    if (typeof e === 'string' && e.toLowerCase() === 'invalid uuid') {
      this.errorBus.emit(new ErrorEvent(ErrorEventName.ValidationError, e));
      return;
    }

    this.errorBus.emit(new ErrorEvent(ErrorEventName.UnknownError, typeof e === 'string' ? e : undefined));
  }

  get lobbyUuid(): string | undefined {
    if (!this.lobby) {
      this.lobby = useLobbyStore();
    }

    return this.lobby.lobbyUuid
  }

  get playerUuid(): string | undefined {
    if (!this.lobby) {
      this.lobby = useLobbyStore();
    }

    return this.lobby.playerUuid
  }

  async getStatus() {
    try {
      const state = await this.post('/status', { lobbyUuid: this.lobbyUuid })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.UpdateState), state)
    } catch (e) {
      this.handleError(e)
    }
  }

  async newGame(): Promise<string> {
    this.logger.debug('Creating new game')
    const response = await this.post('/new-lobby')
    this.logger.debug('New game created: ', response)
    return response.uuid
  }

  async setPlayerLimit(limit: number) {
    try {
      const state = await this.post('/set-player-limit', { lobbyUuid: this.lobbyUuid, limit: limit })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.SetPlayerLimit), state)
    } catch (e) {
      this.handleError(e)
    }
  }

  async setPlayerName(name: string) {
    // TODO: Implement backend
    try {
      const state = await this.post('/set-player-name', {
        lobbyUuid: this.lobbyUuid,
        playerUuid: this.playerUuid,
        name: name
      })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.SetPlayerName), state)
    } catch (e) {
      this.handleError(e)
    }
  }

  async joinLobby(name: string) {
    try {
      const state = await this.post('/join-lobby', {
        lobbyUuid: this.lobbyUuid,
        playerUuid: this.playerUuid,
        name: name
      })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.JoinLobby), state)
    } catch (e) {
      this.handleError(e)
      return Promise.reject()
    }
  }

  async startGame() {
    try {
      const state = await this.post('/start-game', { lobbyUuid: this.lobbyUuid })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.StartGame), state)
    } catch (e) {
      this.handleError(e)
    }
  }

  async setPrediction(prediction: number) {
    try {
      const state = await this.post('/set-prediction', {
        lobbyUuid: this.lobbyUuid,
        playerUuid: this.playerUuid,
        prediction: prediction
      })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.SetPrediction), state)
    } catch (e) {
      this.handleError(e)
    }
  }

  async playCard(card: CardInterface) {
    try {
      const state = await this.post('/play-card', { lobbyUuid: this.lobbyUuid, playerUuid: this.playerUuid, card: card })

      this.gameStateBus.emit(new GameStateEvent(GameStateEventName.PlayCard), state)
    } catch (e) {
      this.handleError(e)
    }
  }
}
