package de.htwg.se.skullking.util

import de.htwg.se.skullking.model.StateComponent.IGameState

trait Command {
  def doStep: IGameState
  def undoStep: IGameState
  def redoStep: IGameState
}
