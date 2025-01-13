import type {EventBusKey} from "@vueuse/core";
import {type GameStateInterface} from "@/core/model/GameState";

export interface Event<T> {
  name: T
}

export enum GameStateEventName {
  GameStateChange,
  PromptPrediction,
  UpdateState,
  SetPlayerLimit,
  SetPlayerName,
  JoinLobby,
  StartGame,
  SetPrediction,
  PlayCard,
}

export class GameStateEvent implements Event<GameStateEventName> {
  constructor(
    public readonly name: GameStateEventName,
    public readonly state?: GameStateInterface,
    public readonly oldState?: GameStateInterface,
  ) {}
}

export const GameStateBus: EventBusKey<GameStateEvent> = Symbol()

export enum ErrorEventName {
  ValidationError,
  WrongInput,
  ServerError,
  NetworkError,
  UnknownError,

}

export class ErrorEvent implements Event<ErrorEventName> {
  constructor(
    public readonly name: ErrorEventName,
    public readonly message?: string,
    public readonly code?: string | number
  ) {}
}

export const ErrorBus: EventBusKey<ErrorEvent> = Symbol()
