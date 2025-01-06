package models.ControllerComponent.LobbyControllerImpl

import de.htwg.se.skullking.controller.ControllerComponent.*
import de.htwg.se.skullking.model.PlayerComponent.{IPlayer, IPlayerFactory}
import de.htwg.se.skullking.model.StateComponent.{IGameState, Phase}
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.util.UndoManager
import models.model.LobbyComponent.LobbyBaseImpl.LobbyObject
import de.htwg.se.skullking.controller.ControllerComponent.BaseControllerImpl.Controller as BaseController

import java.util.UUID

class Controller(using state: IGameState) extends BaseController with ILobbyController {
  override val undoManager = UndoManager()
  private var playerIDs: Int = 0

  def newLobby(uuid: UUID, playerLimit: Int): Unit = {
    //undoManager.doStep(new NewLobbyCommand(this))
    LobbyObject.createLobby(uuid, playerLimit)
    notifyObservers(ControllerEvents.NewLobby)
    //print(state.toJson)
    handleState(state)
  }

  override def joinLobby(player: String, playerUuid: UUID, lobbyUuid: UUID): Boolean = {
    LobbyObject.getLobby(lobbyUuid)
     match
      case None => false
      case Some(lobby) =>
        if (lobby.players.length < lobby.playerLimit) {
          lobby.joinLobby(summon[IPlayerFactory].create(playerUuid, player), lobbyUuid)
          true
        } else {
          false
        }
    
  }
  
  override def leaveLobby(player: IPlayer): Boolean = ???
}
