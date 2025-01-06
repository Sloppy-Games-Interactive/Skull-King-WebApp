package models.ControllerComponent.LobbyControllerImpl

import de.htwg.se.skullking.controller.ControllerComponent.*
import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.FileIOComponent.IFileIO
import de.htwg.se.skullking.model.PlayerComponent.{IPlayer, IPlayerFactory}
import de.htwg.se.skullking.model.StateComponent.{IGameState, Phase}
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.util.UndoManager
import models.model.LobbyComponent.LobbyBaseImpl.Lobby

class Controller(using var state: IGameState) extends IController {
  override val undoManager = UndoManager()
  val lobby: Lobby = Lobby()
  private var playerIDs: Int = 0

  override def handleState(): Unit = {
    state.phase match {
      case Phase.PrepareGame if state.playerLimit == 0 => notifyObservers(ControllerEvents.PromptPlayerLimit)
      case Phase.PrepareGame => notifyObservers(ControllerEvents.PromptPlayerName)
      case Phase.PrepareTricks => notifyObservers(ControllerEvents.PromptPrediction)
      case Phase.PlayTricks => notifyObservers(ControllerEvents.PromptCardPlay)
      case Phase.EndGame => notifyObservers(ControllerEvents.PromptNewGame)
    }
  }

  override def undo: Unit = {
    undoManager.undoStep
    notifyObservers(ControllerEvents.Undo)
    handleState()
  }

  override def redo: Unit = {
    undoManager.redoStep
    notifyObservers(ControllerEvents.Redo)
    handleState()
  }

  override def newGame: Unit = {
    undoManager.doStep(new NewGameCommand(this))
    notifyObservers(ControllerEvents.NewGame)
    handleState()
  }

  def newLobby(name: String, playerLimit: Int): Unit = {
    //undoManager.doStep(new NewLobbyCommand(this))
    lobby.createLobby(name, playerLimit)
    notifyObservers(ControllerEvents.NewLobby)
    //print(state.toJson)
    handleState()
  }
  
  override def setPlayerLimit(limit: Int): Unit = {
    undoManager.doStep(new SetPlayerLimitCommand(this, limit))
    notifyObservers(ControllerEvents.PlayerLimitSet)
    handleState()
  }

  override def addPlayer(name: String): Unit = {
    undoManager.doStep(new AddPlayerCommand(this, summon[IPlayerFactory].create(playerIDs, name)))
    playerIDs += 1
    notifyObservers(ControllerEvents.PlayerAdded)
    handleState()
  }

  override def playCard(player: IPlayer, card: ICard): Unit = {
    undoManager.doStep(new PlayCardCommand(this, player, card))
    notifyObservers(ControllerEvents.CardPlayed)
    handleState()
  }

  override def setPrediction(player: IPlayer, prediction: Int): Unit = {
    undoManager.doStep(new SetPredictionCommand(this, player, prediction))
    notifyObservers(ControllerEvents.PredictionSet)
    handleState()
  }

  override def saveGame(saveState: Option[IGameState] = None): Unit = {
    val stateToSave = saveState match {
      case Some(s) => s
      case None => state
    }
    
    summon[IFileIO].save(stateToSave)
    notifyObservers(ControllerEvents.SaveGame)
    handleState()
  }
  
  override def loadGame(loadState: Option[IGameState] = None): Unit = {
    val stateToLoad = loadState match {
      case Some(s) => s
      case None => summon[IFileIO].load
    }

    undoManager.doStep(new LoadGameCommand(this, stateToLoad))
    notifyObservers(ControllerEvents.LoadGame)
    handleState()
  }
  
  override def quit: Unit = {
    saveGame()
    notifyObservers(ControllerEvents.Quit)
  }
}
