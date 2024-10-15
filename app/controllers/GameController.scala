package controllers

import javax.inject.*
import play.api.*
import play.api.mvc.*
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.controller.ControllerComponent.IController
import de.htwg.se.skullking.view.tui.{Parser, Tui}
import de.htwg.se.skullking.view.gui.Gui

import scala.io.StdIn.readLine
import scala.language.postfixOps

@Singleton
class GameController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val controller: IController = summon[IController]
  val tui = Tui(controller)
  val gui = Gui(controller)

  val parser = new Parser

  new Thread(() => {
    println("Starting Skullking GUI")
    gui.main(Array.empty)
  }).start()

  def newGame = Action { implicit request: Request[AnyContent] =>
    controller.newGame
    Ok(controller.state.toJson)
  }

  def setPlayerLimit = Action { implicit request: Request[AnyContent] =>
    val limit = request.body.asJson.get("limit").as[String]
    parser.parsePlayerLimit(limit) match
      case Some(l) =>
        controller.setPlayerLimit(l)
        Ok(controller.state.toJson)
      case None => BadRequest("Invalid limit")
    Ok(controller.state.toJson)
  }

  def setPlayerName = Action { implicit request: Request[AnyContent] =>
    val name = request.body.asJson.get("name").as[String]
    parser.parsePlayerName(name) match
      case Some(n) =>
        controller.addPlayer(n)
        Ok(controller.state.toJson)
      case None => BadRequest("Invalid name")
  }

  def setPrediction = Action { implicit request: Request[AnyContent] =>
    val prediction = request.body.asJson.get("prediction").as[String]
    controller.state.activePlayer match
      case Some(player) =>
        parser.parsePrediction(prediction, controller.state.round) match
          case Some(pred) =>
            controller.setPrediction(player, pred)
            Ok(controller.state.toJson)
          case None => BadRequest("Invalid prediction")

      case None => BadRequest("No active player")

  }

  def playCard = Action { implicit request: Request[AnyContent] =>
    val card = request.body.asJson.get("card").as[String]
    controller.state.activePlayer match
      case Some(player) =>
        parser.parseCardPlay(card, player) match
          case Some(c) =>
            controller.playCard(player, c)
            Ok(controller.state.toJson)
          case None => BadRequest("Invalid card")
      case None => BadRequest("No active player")
  }

}
