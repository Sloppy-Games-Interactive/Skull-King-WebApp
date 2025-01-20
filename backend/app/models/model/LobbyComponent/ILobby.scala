package models.model.LobbyComponent

import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.skullking.model.StateComponent.IGameState
import de.htwg.se.skullking.modules.Default.given

import java.util.UUID
import scala.collection.mutable

trait ILobby {
  val players: List[IPlayer]
  val playerLimit: Int
  val gameState: IGameState
  val uuid: UUID
  
  def joinLobby(player: IPlayer, uuid: UUID): ILobby
  def leaveLobby(player: IPlayer): Boolean
  def startGame: Unit
  def setGameState(state: IGameState): ILobby
  def setPlayerLimit(limit: Int): ILobby
}

