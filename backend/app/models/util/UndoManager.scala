package de.htwg.se.skullking.util

import de.htwg.se.skullking.model.StateComponent.IGameState

class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil
  def canUndo = undoStack.nonEmpty
  def canRedo = redoStack.nonEmpty

  def doStep(command: Command): IGameState = {
    undoStack = command :: undoStack
    val state = command.doStep

    // Clear the redo stack because redo makes no sense after a new command has been executed
    redoStack = Nil
    state
  }
  def undoStep = {
    undoStack match {
      case Nil =>
      case head :: stack => {
        head.undoStep
        undoStack = stack
        redoStack = head :: redoStack
      }
    }
  }
  def redoStep = {
    redoStack match {
      case Nil =>
      case head :: stack => {
        head.redoStep
        redoStack = stack
        undoStack = head :: undoStack
      }
    }
  }
}
