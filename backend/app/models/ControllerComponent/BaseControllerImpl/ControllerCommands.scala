package de.htwg.se.skullking.controller.ControllerComponent.BaseControllerImpl

import de.htwg.se.skullking.controller.ControllerComponent.*
import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.FileIOComponent.IFileIO
import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.StateComponent.*
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.util.Command

class AddPlayerCommand(val state: IGameState, player: IPlayer) extends Command {
  override def doStep: IGameState = state.handleEvent(AddPlayerEvent(player))
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class NewGameCommand extends Command {
  override def doStep: IGameState = summon[IGameState]
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class LoadGameCommand(val state: IGameState) extends Command {
  override def doStep: IGameState = state
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class PlayCardCommand(val state: IGameState, player: IPlayer, card: ICard) extends Command {
  override def doStep: IGameState = state.handleEvent(PlayCardEvent(player, card))
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class SetPlayerLimitCommand(val state: IGameState, limit: Int) extends Command {
  override def doStep: IGameState = state.handleEvent(SetPlayerLimitEvent(limit))
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class SetPredictionCommand(val state: IGameState, player: IPlayer, prediction: Int) extends Command {
  override def doStep: IGameState = state.handleEvent(SetPredictionEvent(player, prediction))
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}

class StartGameCommand(val state: IGameState) extends Command {
  override def doStep: IGameState = state.handleEvent(StartGameEvent())
  override def undoStep: IGameState = ???
  override def redoStep: IGameState = ???
}
