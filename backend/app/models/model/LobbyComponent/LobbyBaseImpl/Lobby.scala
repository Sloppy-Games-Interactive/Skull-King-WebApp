package models.model.LobbyComponent.LobbyBaseImpl

import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.skullking.model.StateComponent.GameStateBaseImpl.GameState
import de.htwg.se.skullking.model.StateComponent.IGameState
import models.model.LobbyComponent.ILobby

import java.util.UUID
import scala.collection.mutable
import de.htwg.se.skullking.modules.Default.given

object LobbyObject {
  // TODO: HashMap not allowed for controller print, because its not a linked list
  val lobbies: mutable.Map[UUID, ILobby] = mutable.Map()

  def getLobby(uuid: UUID): Option[ILobby] = lobbies.get(uuid)

  def createLobby(uuid: UUID, playerLimit: Int): ILobby = {
    val newLobby = {
      Lobby(uuid = uuid, playerLimit = playerLimit, gameState = summon[IGameState])
    }
    lobbies += (newLobby.uuid -> newLobby)
    newLobby
  }
}

case class Lobby(
  uuid: UUID,
  created: Long = System.currentTimeMillis(),
  // TODO: send modified gameState to specific players with only their hands
  gameState: IGameState = null,
  joinCode: String = scala.util.Random.alphanumeric.take(8).mkString,
  playerLimit: Int = 2,
  players: List[IPlayer] = List(),
  host: IPlayer = null,
  started: Boolean = false
) extends ILobby  {

  override def joinLobby(player: IPlayer, uuid: UUID): Boolean = {
    val lobby = LobbyObject.lobbies(uuid)

    if (players.length < playerLimit) {
      LobbyObject.lobbies += (uuid -> this.copy(players = players :+ player, gameState = lobby.gameState.addPlayer(player)))
      true
    } else {
      false
    }
  }

  override def leaveLobby(player: IPlayer): Boolean = {
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

  private def setPlayerLimit(n: Int): Lobby = this.copy(playerLimit = n)
}