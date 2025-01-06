package de.htwg.se.skullking.controller.ControllerComponent

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.StateComponent.IGameState
import de.htwg.se.skullking.util.{Observable, ObservableEvent, UndoManager}
import models.model.LobbyComponent.ILobby

import java.util.UUID

trait IController() extends Observable {
  var state: IGameState
  val undoManager: UndoManager

  def handleState(): Unit

  def undo: Unit

  def redo: Unit

  def newGame: Unit
  
  def setPlayerLimit(limit: Int): Unit
  
  def addPlayer(name: String): Unit

  def playCard(player: IPlayer, card: ICard): Unit
  
  def setPrediction(player: IPlayer, prediction: Int): Unit
  
  def saveGame(saveState: Option[IGameState] = None): Unit
  
  def loadGame(loadState: Option[IGameState] = None): Unit
  
  def quit: Unit
}

trait ILobbyController extends IController {
  def joinLobby(player: String, playerUuid: UUID, lobbyUuid: UUID): Boolean
  def leaveLobby(player: IPlayer): Boolean
  def newLobby(UUID: UUID, playerLimit: Int): Unit

}

enum ControllerEvents extends ObservableEvent {
  case NewGame
  case NewLobby
  case PromptPlayerLimit
  case PromptPlayerName
  case PlayerAdded
  case PlayerAddedFinished
  case PlayerLimitSet
  case PromptPrediction
  case PredictionSet
  case PromptCardPlay
  case PromptNewGame
  case Quit
  case Undo
  case Redo
  case CardPlayed
  case SaveGame
  case LoadGame
}
