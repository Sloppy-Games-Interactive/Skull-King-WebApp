package controllers

import javax.inject.*
import play.api.*
import play.api.mvc.*
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.controller.ControllerComponent.IController
import de.htwg.se.skullking.model.StateComponent.GameStateDeserializer
import de.htwg.se.skullking.view.tui.{Parser, Tui}
import de.htwg.se.skullking.view.gui.Gui
import play.api.libs.json.JsObject

import scala.io.StdIn.readLine
import scala.language.postfixOps
import scala.util.{Try, Success, Failure}

@Singleton
class GameController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val controller: IController = summon[IController]
  val tui = Tui(controller)
  val gui = Gui(controller)

  val parser = new Parser

  new Thread(() => {
    gui.main(Array.empty)
  }).start()

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(controller.state.toJson)
  }

  def newGame = Action { implicit request: Request[AnyContent] =>
    controller.newGame
    Ok(controller.state.toJson)
  }

  def setPlayerLimit = Action { implicit request: Request[AnyContent] =>
    val limit = Try[String](request.body.asJson.get("limit").as[String])

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

  def setPlayerName = Action { implicit request: Request[AnyContent] =>
    val name = Try[String](request.body.asJson.get("name").as[String])

    name match {
      case Success(n) => parser.parsePlayerName(n)
      case Failure(f) => None
    } match {
      case None => BadRequest("Invalid name")
      case Some(n) =>
        controller.addPlayer(n)
        Ok(controller.state.toJson)
    }
  }

  def setPrediction = Action { implicit request: Request[AnyContent] =>
    val prediction = Try[String](request.body.asJson.get("prediction").as[String])

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
    val card = Try[String](request.body.asJson.get("card").as[String])
    controller.state.activePlayer match {
      case None => BadRequest("No active player")
      case Some(player) =>
        card match {
          case Success(c) => parser.parseCardPlay(c, player)
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
    val stateJson = request.body.asJson

    val stateToLoad = stateJson match
      case Some(json) => Some(GameStateDeserializer.fromJson(json.as[JsObject]))
      case None => None

    controller.loadGame(stateToLoad)
    Ok(controller.state.toJson)
  }
  
  def saveGame = Action { implicit request: Request[AnyContent] =>
    val stateJson = request.body.asJson

    val stateToSave = stateJson match
      case Some(json) => Some(GameStateDeserializer.fromJson(json.as[JsObject]))
      case None => None

    controller.saveGame()
    Ok(controller.state.toJson)
  }
}
