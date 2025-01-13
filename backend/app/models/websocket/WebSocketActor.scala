package models.websocket

import de.htwg.se.skullking.controller.ControllerComponent.{IController, ILobbyController}
import de.htwg.se.skullking.model.StateComponent.IGameState
import de.htwg.se.skullking.util.{ObservableEvent, Observer}
import org.apache.pekko.actor.{Actor, ActorRef, Props}
import org.apache.pekko.stream.Materializer
import de.htwg.se.skullking.modules.Default.given
import models.model.LobbyComponent.ILobby
import models.model.LobbyComponent.LobbyBaseImpl.LobbyObject
import play.api.libs.json.{Format, JsError, JsString, JsSuccess, JsValue, Json}

import java.util.UUID
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object WebSocketEvent extends Enumeration {
  type WebSocketEvent = Value

  val Connected,
      Disconnected,
      State,
      Play,
      Join,
      Leave,
      Message,
      Error,
      SetUuid = Value

  // Not Working???
  implicit val Format: Format[WebSocketEvent] = Json.formatEnum(this)
}

object WebSocketActor {
  def props(out: ActorRef, clients: mutable.Map[String, ActorRef]): Props = Props(new WebSocketActor(out, clients))
  case class SendMessage(message: String)
}

class WebSocketActor(out: ActorRef, clients: mutable.Map[String, ActorRef]) extends Actor with Observer {
  private var clientId: String = UUID.randomUUID().toString
  val controller: ILobbyController = summon[ILobbyController]
  controller.add(this)

  override def update(e: ObservableEvent, state: Option[IGameState] = None): Unit = {
    e match {
      case _ =>
        state match
          case Some(state) => clients.foreach(_._2 ! transportProtocol(WebSocketEvent.State,
            List(UUID.fromString(clientId)), UUID.fromString(clientId), state.toJson).toString)
          case None =>
    }
  }

  override def preStart(): Unit = {
    clients += clientId -> out
    val clientIdJson = Json.obj("playerId" -> clientId)
    out ! transportProtocol(WebSocketEvent.Connected, List(UUID.fromString(clientId)), UUID.fromString(clientId), clientIdJson).toString
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

      (jsonData \ "event").as[WebSocketEvent.WebSocketEvent] match {
        case WebSocketEvent.State => {
          getLobby(jsonData) match {
            case Some(lobby) => {
              out ! transportProtocol(
                WebSocketEvent.State,
                List(UUID.fromString(clientId)),
                UUID.fromString(clientId),
                lobby.gameState.toJson
              ).toString
            }
            case None => out ! Json.obj("error" -> "Lobby not found").toString
          }


          //        case "play" =>
          //          val data = (jsonData \ "data").as[JsValue]
          //          controller.play(data)
          //        case "join" =>
          //          val data = (jsonData \ "data").as[JsValue]
          //          controller.join(data)
          //        case "leave" =>
          //          val data = (jsonData \ "data").as[JsValue]
          //          controller.leave(data)
        }
        case WebSocketEvent.Message => {
          println(s"Received message from client $clientId")
          val data = (jsonData \ "data").as[JsValue]

          getLobby(jsonData) match {
            case Some(lobby) => {
              println(data)
              val lobbyPlayerIds = lobby.players.map(p => p.id)
              clients
                .filter(c => c._1 != clientId && lobbyPlayerIds.contains(c._1))
                .foreach(_._2 ! transportProtocol(
                  WebSocketEvent.Message,
                  lobbyPlayerIds,
                  UUID.fromString(clientId),
                  data).toString
                )
            }
            case None => out ! Json.obj("error" -> "Lobby not found").toString
          }
        }
        case WebSocketEvent.SetUuid => {
          val newClientId = (jsonData \ "data").as[String]
          clients -= clientId
          clientId = newClientId
          clients += newClientId -> out
          out ! Json.obj("playerId" -> newClientId).toString
        }
        case _ => out ! Json.obj("error" -> "Invalid event").toString
      }
  }

  def getLobby(jsonData: JsValue): Option[ILobby] = {
    Try[UUID]((jsonData \ "lobbyUuid").as[UUID]) match {
      case Success(lobbyUuid) => {
        lobbyUuid match {
          case lobbyUUID if lobbyUUID != null => LobbyObject.getLobby(lobbyUUID)
          case _ => None
        }
      }
      case Failure(f) => None
    }


  }

  def transportProtocol(event: WebSocketEvent.WebSocketEvent, toClients: List[UUID], fromClient: UUID, data: JsValue): JsValue = {
    Json.obj(
      "event" -> event.toString,
      "toClients" -> toClients,
      "fromClient" -> fromClient,
      "data" -> data
    )
  }

}