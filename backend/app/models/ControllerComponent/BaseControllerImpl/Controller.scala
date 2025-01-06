package de.htwg.se.skullking.controller.ControllerComponent.BaseControllerImpl

import de.htwg.se.skullking.controller.ControllerComponent.*
import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.FileIOComponent.IFileIO
import de.htwg.se.skullking.model.PlayerComponent.{IPlayer, IPlayerFactory}
import de.htwg.se.skullking.model.StateComponent.{IGameState, Phase}
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.util.UndoManager
import models.model.LobbyComponent.ILobby

class Controller(using var state: IGameState) extends IController {
  val undoManager = UndoManager()

   def handleState(state: IGameState): Unit = {
    state.phase match {
      case Phase.PrepareGame if state.playerLimit == 0 => notifyObservers(ControllerEvents.PromptPlayerLimit, Option(state))
      case Phase.PrepareGame => notifyObservers(ControllerEvents.PromptPlayerName, Option(state))
      case Phase.PrepareTricks => notifyObservers(ControllerEvents.PromptPrediction, Option(state))
      case Phase.PlayTricks => notifyObservers(ControllerEvents.PromptCardPlay, Option(state))
      case Phase.EndGame => notifyObservers(ControllerEvents.PromptNewGame, Option(state))
    }
  }

   def addPlayer(state: IGameState, name: String): IGameState = ??? 

   def newGame: IGameState = {
    val nextState = undoManager.doStep(new NewGameCommand)
    notifyObservers(ControllerEvents.NewGame, Option(nextState))
    handleState(nextState)
    nextState
  }
  
   def setPlayerLimit(state: IGameState, limit: Int): IGameState = {
    val nextState = undoManager.doStep(new SetPlayerLimitCommand(state, limit))
    notifyObservers(ControllerEvents.PlayerLimitSet, Option(nextState))
    handleState(nextState)
     nextState
  }

   def playCard(state: IGameState, player: IPlayer, card: ICard): IGameState = {
    val nextState = undoManager.doStep(new PlayCardCommand(state, player, card))
    notifyObservers(ControllerEvents.CardPlayed, Option(nextState))
    handleState(nextState)
    nextState
  }

   def setPrediction(state: IGameState, player: IPlayer, prediction: Int): IGameState = {
    val nextState = undoManager.doStep(new SetPredictionCommand(state, player, prediction))
    notifyObservers(ControllerEvents.PredictionSet, Option(nextState))
    handleState(nextState)
    nextState
  }

   def saveGame(saveState: Option[IGameState] = None): Unit = {
     saveState match {
      case Some(stateToSave) => {
        summon[IFileIO].save(stateToSave)
        notifyObservers(ControllerEvents.SaveGame, Option(stateToSave))
      }
      case None =>
    }
  }
  
   def loadGame(loadState: Option[IGameState] = None): IGameState = {
    val stateToLoad = loadState match {
      case Some(s) => s
      case None => summon[IFileIO].load
    }
    val nextState = undoManager.doStep(new LoadGameCommand(stateToLoad))
    notifyObservers(ControllerEvents.LoadGame, Option(nextState))
    handleState(nextState)
     nextState
   }
  
   def quit: Unit = {
    saveGame()
    notifyObservers(ControllerEvents.Quit)
  }
}
