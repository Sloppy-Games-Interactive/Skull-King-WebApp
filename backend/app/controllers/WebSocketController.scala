package controllers

import de.htwg.se.skullking.modules.Default.given
import org.apache.pekko.actor.ActorRef
import org.apache.pekko.stream.Materializer

import javax.inject.*
import play.api.mvc.*
import play.api.libs.streams.ActorFlow

import scala.collection.mutable
import models.websocket.WebSocketActor

@Singleton
class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: org.apache.pekko.actor.ActorSystem, mat: Materializer) extends AbstractController(cc) {
  private val clients = mutable.Map[String, ActorRef]()

  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      WebSocketActor.props(out, clients)
    }
  }

  def listClients: Action[AnyContent] = Action {
    val clientIds = clients.keys
    Ok(clientIds.mkString("\n"))
  }

  def sendMessageToClient(clientId: String, message: String): Action[AnyContent] = Action {
    clients.get(clientId) match {
      case Some(clientRef) =>
        clientRef ! WebSocketActor.SendMessage(message)
        Ok(s"Message sent to client $clientId")
      case None =>
        NotFound(s"Client $clientId not found")
    }
  }
}
