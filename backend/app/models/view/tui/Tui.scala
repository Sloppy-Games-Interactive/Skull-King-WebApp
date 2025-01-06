package de.htwg.se.skullking.view.tui

import de.htwg.se.skullking.controller.ControllerComponent.{ControllerEvents, IController}
import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.StateComponent.IGameState
import de.htwg.se.skullking.util.{ObservableEvent, Observer}

import scala.util.{Success, Try}

enum PromptState {
  case None
  case PlayerLimit
  case PlayerName
  case Prediction
  case CardPlay
  case NewGame
}

class Tui(controller: IController) extends Observer {
  var promptState: PromptState = PromptState.None
  
  val prompter = new Prompter
  val parser = new Parser
  
  controller.add(this)

  println("Welcome to Skull King!")
  TuiKeys.values.foreach { key =>
    println(s"${key.productPrefix}, Key: ${key.key}")
  }

  private def printTable(table: List[Seq[Any]] | Seq[Seq[Any]]): Unit = table.foreach { row =>
    println(row.map(_.toString).mkString(" | "))
  }

  def printStatusScreen(state: IGameState): Unit = {
    val players = state.players
    val round = state.round
    val phase = state.phase
    val currentTrick = state.activeTrick

    // print players in table format, columns for player name, prediction, score, and hand
    val playerTable = Seq("Name", "Prediction", "Score", "Hand", "Active") +: players.map { player =>
      Seq(player.name, player.prediction.getOrElse("-"), player.score, player.hand, if (player.active) "X" else "")
    }

    printTable(playerTable)

    println("----------------------------------")
    println()

    val statusTable = Seq(
      Seq("Round", "Phase", "Trick"),
      Seq(round, phase, currentTrick.getOrElse("-"))
    )

    printTable(statusTable)

    println("----------------------------------")
    println()
  }

  override def update(e: ObservableEvent, state: Option[IGameState] = None): Unit = {
    e match {
      case ControllerEvents.Quit => {
        println("Goodbye!")
        System.exit(0)
      }
      case ControllerEvents.PromptPlayerLimit => {
        promptState = PromptState.PlayerLimit
        prompter.promptPlayerLimit
      }
      case ControllerEvents.PromptPlayerName => {
        promptState = PromptState.PlayerName
        prompter.promptPlayerName
      }
      case ControllerEvents.PromptPrediction => {
        state match
          case Some(state) =>
            state.activePlayer match {
              case Some(player) => {
                promptState = PromptState.Prediction
                prompter.promptPrediction(player.name, state.round)
              }
              case None => println("No active player.")
            }
          case None =>
      }
      case ControllerEvents.PromptCardPlay => {
        state match
          case Some(state) =>
            state.activePlayer match {
              case Some(player) => {
                promptState = PromptState.CardPlay
                prompter.promptCardPlay(player)
              }
              case None => println("No active player.")
            }
          case None =>
      }

      case ControllerEvents.LoadGame => {
        println("Game loaded.")
        state match
          case Some(state) => printStatusScreen(state)
          case None =>
      }
      case _ => state match
        case Some(state) => printStatusScreen(state)
        case None =>
    }
  }
}




