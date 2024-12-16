package models.model.LobbyComponent.LobbyBaseImpl

import de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.skullking.model.StateComponent.IGameState
import models.model.LobbyComponent.ILobby
import models.model.LobbyComponent.Lobby.lobbies

import java.util.UUID
import scala.collection.mutable

case class Lobby(
  uuid: UUID = UUID.randomUUID(),
  created: Long = System.currentTimeMillis(),
  // TODO: send modified gameState to specific players with only their hands
  gameState: IGameState = null,
  name: String = "",
  joinCode: String = scala.util.Random.alphanumeric.take(8).mkString,
  playerLimit: Int = 2,
  players: List[Player] = List(),
  host: Player = null,
  started: Boolean = false
) extends ILobby {

  override def createLobby(name: String, playerLimit: Int): ILobby = {
    val newLobby = Lobby(name = name, playerLimit = playerLimit)
    lobbies += (newLobby.uuid -> newLobby)
    newLobby
  }

  override def joinLobby(player: Player, uuid: UUID): Boolean = {
    val lobby = lobbies(uuid)
    if (players.isEmpty) {
      this.copy(players = players :+ player).copy(host = player)
    }

    if (players.length < playerLimit) {
      this.copy(players = players :+ player)
      true
    } else {
      false
    }
  }

  override def leaveLobby(player: Player): Boolean = {
    if (players.contains(player)) {
      val updatedPlayers = players.filterNot(_ == player)
      val updatedHost = if (player == host && updatedPlayers.nonEmpty) updatedPlayers.head else host

      if (player == host && updatedPlayers.isEmpty) {
        false
      } else {
        this.copy(players = updatedPlayers, host = updatedHost)
        true
      }
    } else {
      false
    }
  }

  override def startGame: Unit = {
    this.copy(started = true)
  }

  //override def getLobby: ILobby = this
  override def getLobby: ILobby = lobbies.last._2
  def getLobby(uuid: UUID): ILobby = lobbies(uuid)

  private def setPlayerLimit(n: Int): Lobby = this.copy(playerLimit = n)
}
