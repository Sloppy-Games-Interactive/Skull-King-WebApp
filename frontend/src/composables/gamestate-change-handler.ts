import {EventName, GameStateBus, GameStateEvent} from '@/core/event-bus'
import { useEventBus } from '@vueuse/core'
import { Phase } from '@/core/model/GameState'
import {useGameStateStore} from "@/core/stores/gameState";

export function useGameStateChangeHandler() {
  const gameState = useGameStateStore()
  const bus = useEventBus<GameStateEvent>(GameStateBus)


  gameState.$subscribe((a, state, oldState) => {
    if (state?.phase === Phase.PrepareTricks) {
      bus.emit(new GameStateEvent(EventName.PromptPrediction))
      return;
    }
  })

  // changeBus.on((event: GameStateEvent) => {
  //   const { currentState, oldState } = event
  //
  //   if (currentState?.phase === Phase.PrepareTricks) {
  //     bus.emit(new GameStateEvent(EventName.PromptPrediction))
  //     return;
  //   }
  // })
}
