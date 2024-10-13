package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.controller.ControllerComponent.IController

@Singleton
class TuiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def newGame() = Action { implicit request: Request[AnyContent] =>
    val controller: IController = summon[IController]
    controller.newGame
    Ok(controller.state.toJson)
  }
}
