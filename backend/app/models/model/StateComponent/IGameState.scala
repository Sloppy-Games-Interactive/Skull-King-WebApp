package de.htwg.se.skullking.model.StateComponent

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.DeckComponent.{DeckDeserializer, IDeck}
import de.htwg.se.skullking.model.PlayerComponent.{IPlayer, PlayerDeserializer}
import de.htwg.se.skullking.model.trick.TrickComponent.{ITrick, TrickDeserializer}
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.modules.{Deserializer, Serializable}
import play.api.libs.json.{JsObject, Json}

import java.util.UUID
import scala.xml.Elem

enum Phase {
  case PrepareGame
  case PrepareTricks
  case PlayTricks
  case EndGame
}

trait GameStateEvent()
case class SetPlayerLimitEvent(n: Int) extends GameStateEvent
case class AddPlayerEvent(player: IPlayer) extends GameStateEvent
case class SetPredictionEvent(player: IPlayer, prediction: Int) extends GameStateEvent
case class PlayCardEvent(player: IPlayer, card: ICard) extends GameStateEvent
case class StartGameEvent() extends GameStateEvent


object GameStateDeserializer extends Deserializer[IGameState] {
  private val GameStateFactory = summon[IGameStateFactory]

  override def fromXml(xml: Elem): IGameState = {
    val round = (xml \ "@round").text.toInt
    val phase = (xml \ "@phase").text match {
      case "PrepareGame" => Phase.PrepareGame
      case "PrepareTricks" => Phase.PrepareTricks
      case "PlayTricks" => Phase.PlayTricks
      case "EndGame" => Phase.EndGame
    }
    val playerLimit = (xml \ "@playerLimit").text.toInt
    val roundLimit = (xml \ "@roundLimit").text.toInt
    val deck = DeckDeserializer.fromXml((xml \ "Deck").head.asInstanceOf[Elem])
    val players = (xml \ "Players" \ "Player").map(node => PlayerDeserializer.fromXml(node.head.asInstanceOf[Elem])).toList
    val tricks = (xml \ "Tricks" \ "Trick").map(node => TrickDeserializer.fromXml(node.head.asInstanceOf[Elem])).toList

    GameStateFactory(
      round = round,
      phase = phase,
      playerLimit = playerLimit,
      roundLimit = roundLimit,
      deck = deck,
      players = players,
      tricks = tricks,
      lastTrickWinner = None
    )
  }

  override def fromJson(json: JsObject): IGameState = {
    val round = (json \ "round").as[Int]
    val phase = (json \ "phase").as[String] match {
      case "PrepareGame" => Phase.PrepareGame
      case "PrepareTricks" => Phase.PrepareTricks
      case "PlayTricks" => Phase.PlayTricks
      case "EndGame" => Phase.EndGame
    }
    val playerLimit = (json \ "playerLimit").as[Int]
    val roundLimit = (json \ "roundLimit").as[Int]
    val deck = DeckDeserializer.fromJson((json \ "deck").as[JsObject])
    val players = (json \ "players").as[List[JsObject]].map(PlayerDeserializer.fromJson)
    val tricks = (json \ "tricks").as[List[JsObject]].map(TrickDeserializer.fromJson)
    val lastTrickWinner = (json \ "lastTrickWinner").asOpt[JsObject].map(PlayerDeserializer.fromJson)

    GameStateFactory(
      round = round,
      phase = phase,
      playerLimit = playerLimit,
      roundLimit = roundLimit,
      deck = deck,
      players = players,
      tricks = tricks,
      lastTrickWinner = lastTrickWinner
    )
  }
}

trait IGameState extends Serializable{
  val phase: Phase
  val playerLimit: Int
  val players: List[IPlayer]
  val round: Int
  val tricks: List[ITrick]
  val deck: IDeck
  val roundLimit: Int
  val lastTrickWinner: Option[IPlayer]

  override def toXml: Elem = {
    <GameState round={round.toString} phase={phase.toString} playerLimit={playerLimit.toString} roundLimit={roundLimit.toString}>
        {deck.toXml}
      <Players>
        {players.map(_.toXml)}
      </Players>
      <Tricks>
        {tricks.map(_.toXml)}
      </Tricks>
    </GameState>
  }

  override def toJson: JsObject = {
    Json.obj(
      "round" -> round,
      "phase" -> phase.toString,
      "playerLimit" -> playerLimit,
      "roundLimit" -> roundLimit,
      "players" -> players.map(_.toJson),
      "deck" -> deck.toJson,
      "tricks" -> tricks.map(_.toJson),
      "lastTrickWinner" -> lastTrickWinner.map(_.toJson)
    )
  }
  
  def addPlayer(player: IPlayer): IGameState

  def handleEvent(event: GameStateEvent): IGameState

  def activePlayer: Option[IPlayer]
  
  def activeTrick: Option[ITrick]
  
  def sanitizeState(player: Option[IPlayer | String | UUID]): IGameState
}

trait IGameStateFactory {
  def apply(round: Int, phase: Phase, playerLimit: Int, roundLimit: Int, deck: IDeck, players: List[IPlayer], tricks: List[ITrick], lastTrickWinner: Option[IPlayer]): IGameState
}