package models.model.LobbyComponent

import de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl.Player

trait ILobby {
  def createLobby(name: String, playerLimit: Int): ILobby
  def joinLobby(player: Player): Boolean
  def leaveLobby(player: Player): Boolean
  def startGame: Unit
  def getLobby: ILobby
}
