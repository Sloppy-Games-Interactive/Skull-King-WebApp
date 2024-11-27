package controllers

import akka.actor.{Actor, ActorRef, Props}
import akka.stream.Materializer
import javax.inject._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import scala.collection.mutable

@Singleton
class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: akka.actor.ActorSystem, mat: Materializer) extends AbstractController(cc) {

  private val clients = mutable.Map[String, ActorRef]()

  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      WebSocketActor.props(out, clients)
    }
  }
}

object WebSocketActor {
  def props(out: ActorRef, clients: mutable.Map[String, ActorRef]): Props = Props(new WebSocketActor(out, clients))
}

class WebSocketActor(out: ActorRef, clients: mutable.Map[String, ActorRef]) extends Actor {
  override def preStart(): Unit = {
    val clientId = self.path.name
    clients += (clientId -> out)
  }

  override def postStop(): Unit = {
    val clientId = self.path.name
    clients -= clientId
  }

  def receive: Receive = {
    case msg: String =>
      // Handle incoming messages and notify specific clients
      val clientId = extractClientId(msg)
      clients.get(clientId).foreach(_ ! s"Message for $clientId: $msg")
  }

  private def extractClientId(msg: String): String = {
    // Extract client ID from the message (implementation depends on your message format)
    msg.split(":")(0)
  }
}
