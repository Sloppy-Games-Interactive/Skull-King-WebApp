package models.websocket

import de.htwg.se.skullking.controller.ControllerComponent.IController
import de.htwg.se.skullking.util.{ObservableEvent, Observer}
import org.apache.pekko.actor.{Actor, ActorRef, Props}
import org.apache.pekko.stream.Materializer
import de.htwg.se.skullking.modules.Default.given
import play.api.libs.json.Json

import java.util.UUID
import scala.collection.mutable

enum WebSocketEvent {
  case Connected
  case Disconnected
  case State
  case Play
  case Join
  case Leave
  case Message
  case Error
}

object WebSocketActor {
  def props(out: ActorRef, clients: mutable.Map[String, ActorRef]): Props = Props(new WebSocketActor(out, clients))
  case class SendMessage(message: String)
}

class WebSocketActor(out: ActorRef, clients: mutable.Map[String, ActorRef]) extends Actor with Observer {
  private val clientId: String = UUID.randomUUID().toString
  val controller: IController = summon[IController]
  controller.add(this)

  override def update(e: ObservableEvent): Unit = {
    e match {
      case _ => clients.foreach(_._2 ! controller.state.toJson.toString)
    }
  }

  override def preStart(): Unit = {
    clients += clientId -> out
    val clientIdJson = Json.obj("playerId" -> clientId)
    out ! clientIdJson.toString
    println(s"Connected with client $clientId")
    //out ! s"Connected with client $clientId"
  }

  override def postStop(): Unit = {
    println(s"Disconnected with client $clientId")
    clients -= clientId
  }

  def receive: Receive = {
    case msg: String if msg.equals("ping") =>
      //println("WebSocketActor receive from clientid " + clientId)
      out ! "pong"
    case msg: String if msg.equals("state") =>
      println("WebSocketActor receive from clientid " + clientId)
      out ! controller.state.toJson.toString
    case msg: String =>
      // Handle incoming messages and notify specific clients
      println("WebSocketActor receive from clientid " + this.clientId)
      val clientId = extractClientId(msg)
      // TODO: Extend for multiple clients
      clients.get(clientId).foreach(_ ! s"Message for $clientId: $msg")
  }

  private def extractClientId(msg: String): String = {
    // Extract client ID from the message (implementation depends on your message format)
    msg.split(":")(0)
  }

}