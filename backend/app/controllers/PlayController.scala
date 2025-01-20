package controllers

import javax.inject.*
import play.api.*
import play.api.mvc.*
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.controller.ControllerComponent.{IController, ILobbyController}
import de.htwg.se.skullking.util.RoutesUtil
import de.htwg.se.skullking.view.tui.{Parser, Tui}
import models.model.LobbyComponent.ILobby
import models.model.LobbyComponent.LobbyBaseImpl.{Lobby, LobbyObject}
import play.api.libs.json.{JsObject, Json}

import java.util.UUID
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

@Singleton
class PlayController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val controller: ILobbyController = summon[ILobbyController]
  val tui = Tui(controller)
  
  val parser = new Parser

  def index = Action {
    val routes = RoutesUtil.getRoutes
    Ok(views.html.index(routes))
  }

  def rules = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }
  
  def newLobby = Action { implicit request: Request[AnyContent] =>
    val uuid = UUID.randomUUID()
    controller.newLobby(uuid, 2)
    // print last added lobby (maybe vulnerable to race conditions exploits)
    // print uuid as json
    Ok(Json.toJson(Map("uuid" -> uuid.toString)))
  }

  def getStatus() = Action { implicit request: Request[AnyContent] =>
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])
    
    val player = playerUuid match
      case Success(p) => Some(p)
      case Failure(f) => None

    lobbyAction(uuid, (lobby: ILobby) => {
      Ok(lobby.gameState.sanitizeState(player).toJson)
    })
  }

  def setPlayerLimit = Action { implicit request: Request[AnyContent] =>
    val limit = Try[Int](request.body.asJson.get("limit").as[Int])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])

    val player = playerUuid match
      case Success(p) => Some(p)
      case Failure(f) => None

    lobbyAction(uuid, (lobby: ILobby) => {
      limit match {
        case Success(l) => parser.parsePlayerLimit(l)
        case Failure(f) => None
      } match {
        case None => BadRequest("Invalid limit")
        case Some(l) =>
          val nextState = controller.setPlayerLimit(lobby.gameState, l)
          LobbyObject.setLobby(lobby.uuid, lobby.setGameState(nextState).setPlayerLimit(l))
          Ok(nextState.sanitizeState(player).toJson)
      }
    })
  }

  // TODO: Dont change phase automatically when player limit is reached
  // TODO: Adding two players with same name and uuid breaks state
  def joinLobby = Action { implicit request: Request[AnyContent] =>
    val name = Try[String](request.body.asJson.get("name").as[String])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])
    val lobbyUuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])

    lobbyAction(lobbyUuid, (lobby: ILobby) => {
      name match {
        case Success(n) => parser.parsePlayerName(n)
        case Failure(f) => None
      } match {
        case None => BadRequest("Invalid name")
        case Some(n) => {
          parseUuid(playerUuid, (u: UUID) => {
            val nextState = controller.joinLobby(n, u, lobby)
            Ok(nextState.sanitizeState(Some(u)).toJson)
          })
        }
      }
    })
  }

  def setPrediction = Action { implicit request: Request[AnyContent] =>
    val prediction = Try[Int](request.body.asJson.get("prediction").as[Int])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])

    lobbyAction(uuid, (lobby: ILobby) => {

      parseUuid(playerUuid, (u: UUID) => {
        lobby.gameState.players.find(p => p.id == u) match {
          case None => BadRequest("Player not found")
          case Some(player) =>
            prediction match {
              case Success(p) => parser.parsePrediction(p, lobby.gameState.round)
              case Failure(f) => None
            } match {
              case None => BadRequest("Invalid prediction")
              case Some(pred) =>
                val nextState = controller.setPrediction(lobby.gameState, player, pred)
                LobbyObject.setLobby(lobby.uuid, lobby.setGameState(nextState))
                Ok(nextState.sanitizeState(Some(u)).toJson)
            }
        }
      })
    })
  }

  def playCard = Action { implicit request: Request[AnyContent] =>
    val card = Try[JsObject](request.body.asJson.get("card").as[JsObject])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])

    lobbyAction(uuid, (lobby: ILobby) => {

      parseUuid(playerUuid, (u: UUID) => {
        lobby.gameState.players.find(p => p.id == u) match {
          case None => BadRequest("Player not found")
          case Some(player) =>
            card match {
              case Success(c) => parser.parseCardPlay(c)
              case Failure(f) => None
            } match {
              case None => BadRequest("Invalid card")
              case Some(c) => {
                lobby.gameState.activePlayer match {
                  case None => BadRequest("No active player")
                  case Some(activePlayer) => {
                    if (player.id == activePlayer.id) {
                      val nextState = controller.playCard(lobby.gameState, player, c)
                      LobbyObject.setLobby(lobby.uuid, lobby.setGameState(nextState))
                      Ok(nextState.sanitizeState(Some(u)).toJson)
                    } else {
                      BadRequest("Not your turn")
                    }
                  }
                }

              }
            }
        }
      })
    })

  }

  def play = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.playSkullKing())
  }

  def startGame = Action { implicit request: Request[AnyContent] =>
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])
    val playerUuid = Try[UUID](request.body.asJson.get("playerUuid").as[UUID])

    val player = playerUuid match
      case Success(p) => Some(p)
      case Failure(f) => None

    lobbyAction(uuid, (lobby: ILobby) => {
      val nextState = controller.startGame(lobby.gameState)
      LobbyObject.setLobby(lobby.uuid, lobby.setGameState(nextState))
      Ok(nextState.sanitizeState(player).toJson)
    })
  }

  private def lobbyAction(uuid: Try[UUID], lambda: (ILobby) => Result): Result = {
    parseUuid(uuid, (u: UUID) => {
      findLobby(u, (lobby: ILobby) => {
        lambda(lobby)
      })
    })
  }

  private def findLobby(uuid: UUID, lambda: (ILobby) => Result): Result = {
    LobbyObject.getLobby(uuid) match {
      case Some(lobby) => lambda(lobby)
      case None => BadRequest("Lobby not found")
    }
  }

  private def parseUuid(uuid: Try[UUID], success: (UUID) => Result): Result = {
    uuid match {
      case Success(u) => success(u)
      case Failure(f) => BadRequest("Invalid uuid")
    }
  }

}
