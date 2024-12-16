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
import play.api.libs.json.JsObject

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

  def status = Action { implicit request: Request[AnyContent] =>
    request.contentType match {
      case Some("application/json") => Ok(controller.state.toJson)
      case _ => Ok(views.html.status(controller.state))
    }
  }

  def newGame = Action { implicit request: Request[AnyContent] =>
    controller.newGame
    Ok(controller.state.toJson)
  }
  
  def newLobby = Action { implicit request: Request[AnyContent] =>
    val uuid = UUID.randomUUID()
    controller.newLobby(uuid, 2)
    // print last added lobby (maybe vulnerable to race conditions exploits)
    Ok(uuid.toString)
  }
  
  
  def setPlayerLimit = Action { implicit request: Request[AnyContent] =>
    val limit = Try[Int](request.body.asJson.get("limit").as[Int])

    limit match {
      case Success(l) => parser.parsePlayerLimit(l)
      case Failure(f) => None
    } match {
      case None => BadRequest("Invalid limit")
      case Some(l) =>
        controller.setPlayerLimit(l)
        Ok(controller.state.toJson)
    }
  }

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

    controller.state.activePlayer match {
      case None => BadRequest("No active player")
      case Some(player) =>
        prediction match {
          case Success(p) => parser.parsePrediction(p, controller.state.round)
          case Failure(f) => None
        } match {
          case None => BadRequest("Invalid prediction")
          case Some(pred) =>
            controller.setPrediction(player, pred)
            Ok(controller.state.toJson)
        }
    }
  }

  def playCard = Action { implicit request: Request[AnyContent] =>
    val card = Try[JsObject](request.body.asJson.get("card").as[JsObject])
    controller.state.activePlayer match {
      case None => BadRequest("No active player")
      case Some(player) =>
        card match {
          case Success(c) => parser.parseCardPlay(c)
          case Failure(f) => None
        } match {
          case None => BadRequest("Invalid card")
          case Some(c) =>
            controller.playCard(player, c)
            Ok(controller.state.toJson)
        }
    }
  }

  def loadGame = Action { implicit request: Request[AnyContent] =>
    // TODO load from token
    val stateJson = request.body.asJson

    val stateToLoad = stateJson match
      case Some(json) => Some(GameStateDeserializer.fromJson(json.as[JsObject]))
      case None => None

    controller.loadGame(stateToLoad)
    Ok(controller.state.toJson)
  }
  
  def saveGame = Action { implicit request: Request[AnyContent] =>
    // TODO save with token
    controller.saveGame()
    Ok(controller.state.toJson)
  }

  def play = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.playSkullKing())
  }
}
