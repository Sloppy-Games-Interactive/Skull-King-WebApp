import {GameStateBus, GameStateEvent} from '@/core/event-bus'
import { useEventBus } from '@vueuse/core'
import {useGameStateStore} from "@/core/stores/gameState";

export function useGameStateChangeHandler() {
  const gameState = useGameStateStore()
  const bus = useEventBus<GameStateEvent>(GameStateBus)

  bus.on((event: GameStateEvent) => {
    if (!event.state) {
      return;
    }

    gameState.updateGameState(event.state);
  })
}
