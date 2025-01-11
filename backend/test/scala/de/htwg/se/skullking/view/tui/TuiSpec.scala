package de.htwg.se.skullking.view.tui

import de.htwg.se.skullking.controller.ControllerComponent.BaseControllerImpl.Controller
import de.htwg.se.skullking.controller.ControllerComponent.ControllerEvents
import de.htwg.se.skullking.model.StateComponent.GameStateBaseImpl.GameState
import de.htwg.se.skullking.model.StateComponent.Phase
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec extends AnyWordSpec {
  "A Tui" when {
    val initialGameState = GameState(
      phase = Phase.PrepareGame,
      players = List(),
      playerLimit = 2
    )

    "receiving controller events" should {
      "update promptState for PlayerLimitEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)

        tui.update(ControllerEvents.PromptPlayerLimit)
        tui.promptState should be(PromptState.PlayerLimit)
      }

      "update promptState for PlayerNameEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)

        tui.update(ControllerEvents.PromptPlayerName)
        tui.promptState should be(PromptState.PlayerName)
      }

      "update promptState for PredictionEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)
        controller.addPlayer("Alice")
        controller.addPlayer("Bob")

        tui.update(ControllerEvents.PromptPrediction)
        tui.promptState should be(PromptState.Prediction)
      }

      "print error for no active players in PredictionEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)

        val out = new java.io.ByteArrayOutputStream()
        Console.withOut(out) {
          tui.update(ControllerEvents.PromptPrediction)
        }

        out.toString() should include("No active player.")
      }

      "update promptState for CardPlayEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)
        controller.addPlayer("Alice")
        controller.addPlayer("Bob")
        controller.setPrediction(controller.state.players.head, 1)
        controller.setPrediction(controller.state.players(1), 1)

        tui.update(ControllerEvents.PromptCardPlay)
        tui.promptState should be(PromptState.CardPlay)
      }

      "print error for no active players in CardPlayEvent" in {
        val controller = Controller(using initialGameState)
        val tui: Tui = Tui(controller)

        val out = new java.io.ByteArrayOutputStream()
        Console.withOut(out) {
          tui.update(ControllerEvents.PromptCardPlay)
        }

        out.toString() should include("No active player.")
      }
    }
  }
}