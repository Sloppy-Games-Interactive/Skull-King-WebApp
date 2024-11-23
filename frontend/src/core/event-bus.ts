import type {EventBusKey} from "@vueuse/core";
import {type GameStateInterface} from "@/core/model/GameState";


export enum EventName {
  GameStateChange = 'game-state-change',
  PromptPrediction = 'prompt-prediction',
}

export interface Event {
  name: EventName
}

export class GameStateEvent implements Event {
  constructor(
    public readonly name: EventName,
    public readonly currentState?: GameStateInterface,
    public readonly oldState?: GameStateInterface
  ) {}
}

export const GameStateStoreChangeBus: EventBusKey<GameStateEvent> = Symbol()

export const GameStateBus: EventBusKey<GameStateEvent> = Symbol()
