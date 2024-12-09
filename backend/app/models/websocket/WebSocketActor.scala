package models.websocket

import de.htwg.se.skullking.controller.ControllerComponent.IController
import de.htwg.se.skullking.util.{ObservableEvent, Observer}
import org.apache.pekko.actor.{Actor, ActorRef, Props}
import org.apache.pekko.stream.Materializer
import de.htwg.se.skullking.modules.Default.given
import play.api.libs.json.{Format, JsError, JsString, JsSuccess, JsValue, Json}

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

  // Not Working???
  implicit val format: Format[WebSocketEvent] = new Format[WebSocketEvent] {
    def reads(json: JsValue) = json match {
      case JsString(s) => JsSuccess(WebSocketEvent.valueOf(s))
      case _ => JsError("Invalid WebSocketEvent")
    }
    def writes(event: WebSocketEvent) = JsString(event.toString)
  }
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
      case _ => clients.foreach(_._2 ! transportProtocol(WebSocketEvent.State, 
        List(UUID.fromString(clientId)), UUID.fromString(clientId), controller.state.toJson).toString)
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
    case msg: String if msg == "ping" =>
      out ! "pong"
    case msg: String =>
      println(s"Received message from client $clientId: " + msg)
      // TODO: Implement error handling if for example [object Object] is sent
      val jsonData = Json.parse(msg)
      // TODO: fix as[String] to as[WebSocketEvent]
      val event = (jsonData \ "event").as[String]
      event match {
        case event if event == WebSocketEvent.State.toString =>
          out ! transportProtocol(WebSocketEvent.State, List(UUID.fromString(clientId)), 
            UUID.fromString(clientId), controller.state.toJson).toString
//        case "play" =>
//          val data = (jsonData \ "data").as[JsValue]
//          controller.play(data)
//        case "join" =>
//          val data = (jsonData \ "data").as[JsValue]
//          controller.join(data)
//        case "leave" =>
//          val data = (jsonData \ "data").as[JsValue]
//          controller.leave(data)
          // TODO: user WebSocketEvent if fixed (not string)
        case event if event == WebSocketEvent.Message.toString =>
          println(s"Received message from client $clientId")
          val data = (jsonData \ "data").as[JsValue]
          println(data)
          val client = (jsonData \ "toClients").as[List[UUID]]
          // TODO: allow for multiple clients not just the first one
          clients.get(client.head.toString).foreach(_ ! transportProtocol(WebSocketEvent.Message, client, UUID.fromString(clientId), data).toString)

        case _ =>
          out ! Json.obj("error" -> "Invalid event").toString
      }
  }

  def transportProtocol(event: WebSocketEvent, toClients: List[UUID], fromClient: UUID, data: JsValue): JsValue = {
    Json.obj(
      "event" -> event.toString,
      "toClients" -> toClients,
      "fromClient" -> fromClient,
      "data" -> data
    )
  }

}