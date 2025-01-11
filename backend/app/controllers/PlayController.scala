package controllers

import javax.inject.*
import play.api.*
import play.api.mvc.*
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.controller.ControllerComponent.{IController, ILobbyController}
import de.htwg.se.skullking.model.StateComponent.GameStateDeserializer
import de.htwg.se.skullking.util.RoutesUtil
import de.htwg.se.skullking.view.tui.{Parser, Tui}
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
  //val uuid = request.cookies.get("lobbyUuid"
  uuid match
    case Success(u) => LobbyObject.getLobby(u) match {
      case Some(lobby) => Ok(lobby.gameState.toJson: JsObject)
      case None => BadRequest("Invalid lobby uuid")
    }
    case Failure(f) => BadRequest("Invalid lobby uuid")
  }

  def setPlayerLimit = Action { implicit request: Request[AnyContent] =>
    val limit = Try[Int](request.body.asJson.get("limit").as[Int])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])

    uuid match
      case Success(u) => LobbyObject.getLobby(u) match {
        case Some(lobby) => {
          limit match {
            case Success(l) => parser.parsePlayerLimit(l)
            case Failure(f) => None
          } match {
            case None => BadRequest("Invalid limit")
            case Some(l) =>
              val state = controller.setPlayerLimit(lobby.gameState, l)
              val newLobby = lobby.setGameState(state)
              LobbyObject.setLobby(u, newLobby)
              Ok(state.toJson)
          }
        }
        case None => BadRequest("Invalid lobby uuid")
      }
      case Failure(f) => BadRequest("Invalid lobby uuid")


  }

  // TODO: Dont change phase automatically when player limit is reached
  // TODO: Adding two players with same name and uuid breaks state
  def joinLobby = Action { implicit request: Request[AnyContent] =>
    val name = Try[String](request.body.asJson.get("name").as[String])
    val playerUuid = Try[String](request.body.asJson.get("playerUuid").as[String])
    val lobbyUuid = Try[String](request.body.asJson.get("lobbyUuid").as[String])

    name match {
      case Success(n) => parser.parsePlayerName(n)
      case Failure(f) => None
    } match {
      case None => BadRequest("Invalid name")
      case Some(n) =>
        playerUuid match {
          case Success(p) => parser.parsePlayerUUID(p) match {
            case Some(uuid) => lobbyUuid match {
              case Success(l) => parser.parsePlayerUUID(l) match {
                case Some(lobbyUuid) => {
                  controller.joinLobby(n, UUID.fromString(uuid), UUID.fromString(lobbyUuid))
                  Ok(LobbyObject.getLobby(UUID.fromString(lobbyUuid)).get.gameState.toJson)
                }

                case None => BadRequest("Invalid lobby uuid")
              }
              case Failure(f) => BadRequest("Invalid lobby uuid")
            }
            case None => BadRequest("Invalid player uuid")
          }
          case Failure(f) => BadRequest("Invalid player uuid")
        } 
    }
  }

  def setPrediction = Action { implicit request: Request[AnyContent] =>
    val prediction = Try[Int](request.body.asJson.get("prediction").as[Int])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])

    uuid match
      case Success(u) => LobbyObject.getLobby(u) match {
        case Some(lobby) => {
          lobby.gameState.activePlayer match {
            case None => BadRequest("No active player")
            case Some(player) =>
              prediction match {
                case Success(p) => parser.parsePrediction(p, lobby.gameState.round)
                case Failure(f) => None
              } match {
                case None => BadRequest("Invalid prediction")
                case Some(pred) =>
                  val nextState = controller.setPrediction(lobby.gameState, player, pred)
                  LobbyObject.setLobby(u, lobby.setGameState(nextState))
                  Ok(nextState.toJson)
              }
          }
        }
        case None => 
          println("Keine Lobby gefunden: " + u)
          BadRequest("Lobby not found")
      }
      case Failure(f) => 
        println("Invalid lobby uuid: " + f)
        BadRequest("Invalid lobby uuid")


  }

  def playCard = Action { implicit request: Request[AnyContent] =>
    val card = Try[JsObject](request.body.asJson.get("card").as[JsObject])
    val uuid = Try[UUID](request.body.asJson.get("lobbyUuid").as[UUID])

    uuid match
      case Success(u) => LobbyObject.getLobby(u) match {
        case Some(lobby) => {
          lobby.gameState.activePlayer match {
            case None => BadRequest("No active player")
            case Some(player) =>
              card match {
                case Success(c) => parser.parseCardPlay(c)
                case Failure(f) => None
              } match {
                case None => BadRequest("Invalid card")
                case Some(c) =>
                  val nextState = controller.playCard(lobby.gameState, player, c)
                  LobbyObject.setLobby(u, lobby.setGameState(nextState))
                  Ok(nextState.toJson)
              }
          }
        }
        case None => BadRequest("Invalid lobby uuid")
      }
      case Failure(f) => BadRequest("Invalid lobby uuid")


  }

  def play = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.playSkullKing())
  }
}
