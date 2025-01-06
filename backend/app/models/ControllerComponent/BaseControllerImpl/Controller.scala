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

  override def addPlayer(name: String): Unit = ???

   def newGame: Unit = {
    state = undoManager.doStep(new NewGameCommand(state))
    notifyObservers(ControllerEvents.NewGame, Option(state))
    handleState(state)
  }
  
   def setPlayerLimit(limit: Int): Unit = {
    state = undoManager.doStep(new SetPlayerLimitCommand(state, limit))
    notifyObservers(ControllerEvents.PlayerLimitSet, Option(state))
    handleState(state)
  }

   def playCard(player: IPlayer, card: ICard): Unit = {
    state = undoManager.doStep(new PlayCardCommand(state, player, card))
    notifyObservers(ControllerEvents.CardPlayed, Option(state))
    handleState(state)
  }

   def setPrediction(player: IPlayer, prediction: Int): Unit = {
    state = undoManager.doStep(new SetPredictionCommand(state, player, prediction))
    notifyObservers(ControllerEvents.PredictionSet, Option(state))
    handleState(state)
  }

   def saveGame(saveState: Option[IGameState] = None): Unit = {
    val stateToSave = saveState match {
      case Some(s) => s
      case None => state
    }
    
    summon[IFileIO].save(stateToSave)
    notifyObservers(ControllerEvents.SaveGame, Option(state))
    handleState(state)
  }
  
   def loadGame(loadState: Option[IGameState] = None): Unit = {
    val stateToLoad = loadState match {
      case Some(s) => s
      case None => summon[IFileIO].load
    }

    state = undoManager.doStep(new LoadGameCommand(state, stateToLoad))
    notifyObservers(ControllerEvents.LoadGame, Option(state))
    handleState(state)
  }
  
   def quit: Unit = {
    saveGame()
    notifyObservers(ControllerEvents.Quit)
  }
}
