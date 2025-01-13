package de.htwg.se.skullking.controller.ControllerComponent

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.StateComponent.IGameState
import de.htwg.se.skullking.util.{Observable, ObservableEvent, UndoManager}
import models.model.LobbyComponent.ILobby

import java.util.UUID

trait IController() extends Observable {
  val undoManager: UndoManager

  def handleState(state: IGameState): Unit

  def newGame: IGameState
  
  def setPlayerLimit(state: IGameState, limit: Int): IGameState
  
  def addPlayer(state: IGameState, name: String): IGameState

  def playCard(state: IGameState, player: IPlayer, card: ICard): IGameState
  
  def setPrediction(state: IGameState, player: IPlayer, prediction: Int): IGameState
  
  def saveGame(saveState: Option[IGameState] = None): Unit
  
  def loadGame(loadState: Option[IGameState] = None): IGameState
  
  def startGame(state: IGameState): IGameState
  
  def quit: Unit
}

trait ILobbyController extends IController {
  def joinLobby(player: String, playerUuid: UUID, lobby: ILobby): IGameState
  def leaveLobby(player: IPlayer): IGameState
  def newLobby(UUID: UUID, playerLimit: Int): Unit

}

enum ControllerEvents extends ObservableEvent() {
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
