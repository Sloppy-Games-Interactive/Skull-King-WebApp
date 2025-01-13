package de.htwg.se.skullking.util

import de.htwg.se.skullking.model.StateComponent.IGameState

trait Observer {
    def update(e:ObservableEvent, state: Option[IGameState]): Unit
}

class ObservableEvent {}

class Observable {
  var subscribers:Vector[Observer] = Vector()
  def add(s:Observer): Unit = subscribers = subscribers :+ s
  def remove(s:Observer): Unit = subscribers = subscribers.filterNot(o=>o==s)
  def notifyObservers(e:ObservableEvent = ObservableEvent(), state: Option[IGameState] = None): Unit = subscribers.foreach(o => o.update(e, state))
}
