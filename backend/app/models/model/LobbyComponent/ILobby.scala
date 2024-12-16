package models.model.LobbyComponent

import de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl.Player

import java.util.UUID
import scala.collection.mutable

trait ILobby {
  def createLobby(name: String, playerLimit: Int): ILobby
  def joinLobby(player: Player, uuid: UUID): Boolean
  def leaveLobby(player: Player): Boolean
  def startGame: Unit
  def getLobby: ILobby
}

object Lobby {
  // TODO: HashMap not allowed for controller print, because its not a linked list
  val lobbies: mutable.Map[UUID, ILobby] = mutable.Map()
}