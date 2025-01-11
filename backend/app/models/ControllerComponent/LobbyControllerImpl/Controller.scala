package models.ControllerComponent.LobbyControllerImpl

import de.htwg.se.skullking.controller.ControllerComponent.*
import de.htwg.se.skullking.model.PlayerComponent.{IPlayer, IPlayerFactory}
import de.htwg.se.skullking.model.StateComponent.{IGameState, Phase}
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.util.UndoManager
import models.model.LobbyComponent.LobbyBaseImpl.LobbyObject
import de.htwg.se.skullking.controller.ControllerComponent.BaseControllerImpl.Controller as BaseController
import models.model.LobbyComponent.ILobby

import java.util.UUID

// TODO: Remove this class and use the LobbyObject directly
class Controller extends BaseController with ILobbyController {
  override val undoManager = UndoManager()

  def newLobby(uuid: UUID, playerLimit: Int): Unit = {
    //undoManager.doStep(new NewLobbyCommand(this))
    LobbyObject.createLobby(uuid, playerLimit)
    notifyObservers(ControllerEvents.NewLobby)
    //print(state.toJson)
    handleState(summon[IGameState])
  }



  override def joinLobby(player: String, playerUuid: UUID, lobby: ILobby): IGameState = {
        if (lobby.players.length < lobby.playerLimit) {
          val nextLobby = lobby.joinLobby(summon[IPlayerFactory].create(playerUuid, player), lobby.uuid)
          notifyObservers(ControllerEvents.PlayerAdded, Option(nextLobby.gameState))
          handleState(nextLobby.gameState)
          nextLobby.gameState
        } else {
          lobby.gameState
        }
  }
  
  override def leaveLobby(player: IPlayer): IGameState = ???
}
