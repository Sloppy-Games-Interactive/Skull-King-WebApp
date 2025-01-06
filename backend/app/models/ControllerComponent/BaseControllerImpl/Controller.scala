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
  
   def handleState(): Unit = {
    state.phase match {
      case Phase.PrepareGame if state.playerLimit == 0 => notifyObservers(ControllerEvents.PromptPlayerLimit)
      case Phase.PrepareGame => notifyObservers(ControllerEvents.PromptPlayerName)
      case Phase.PrepareTricks => notifyObservers(ControllerEvents.PromptPrediction)
      case Phase.PlayTricks => notifyObservers(ControllerEvents.PromptCardPlay)
      case Phase.EndGame => notifyObservers(ControllerEvents.PromptNewGame)
    }
  }

  override def addPlayer(name: String): Unit = ???

   def undo: Unit = {
    undoManager.undoStep
    notifyObservers(ControllerEvents.Undo)
    handleState()
  }

   def redo: Unit = {
    undoManager.redoStep
    notifyObservers(ControllerEvents.Redo)
    handleState()
  }

   def newGame: Unit = {
    state = undoManager.doStep(new NewGameCommand(state))
    notifyObservers(ControllerEvents.NewGame)
    handleState()
  }
  
   def setPlayerLimit(limit: Int): Unit = {
    state = undoManager.doStep(new SetPlayerLimitCommand(state, limit))
    notifyObservers(ControllerEvents.PlayerLimitSet)
    handleState()
  }

   def playCard(player: IPlayer, card: ICard): Unit = {
    state = undoManager.doStep(new PlayCardCommand(state, player, card))
    notifyObservers(ControllerEvents.CardPlayed)
    handleState()
  }

   def setPrediction(player: IPlayer, prediction: Int): Unit = {
    state = undoManager.doStep(new SetPredictionCommand(state, player, prediction))
    notifyObservers(ControllerEvents.PredictionSet)
    handleState()
  }

   def saveGame(saveState: Option[IGameState] = None): Unit = {
    val stateToSave = saveState match {
      case Some(s) => s
      case None => state
    }
    
    summon[IFileIO].save(stateToSave)
    notifyObservers(ControllerEvents.SaveGame)
    handleState()
  }
  
   def loadGame(loadState: Option[IGameState] = None): Unit = {
    val stateToLoad = loadState match {
      case Some(s) => s
      case None => summon[IFileIO].load
    }

    state = undoManager.doStep(new LoadGameCommand(state, stateToLoad))
    notifyObservers(ControllerEvents.LoadGame)
    handleState()
  }
  
   def quit: Unit = {
    saveGame()
    notifyObservers(ControllerEvents.Quit)
  }
}
