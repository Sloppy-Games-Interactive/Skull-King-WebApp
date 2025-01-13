package de.htwg.se.skullking.model.PlayerComponent

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.modules.Default.given
import de.htwg.se.skullking.modules.{Deserializer, Serializable}
import play.api.libs.json.{JsObject, Json}

import java.util.UUID
import scala.xml.Elem

object PlayerDeserializer extends Deserializer[IPlayer] {
  private val PlayerFactory = summon[IPlayerFactory]

  override def fromXml(xml: Elem): IPlayer = {
    val id = UUID.fromString((xml \ "id").text)
    val name = (xml \ "name").text
    val profilePicUrl = (xml \ "profilePicUrl").text
    val score = (xml \ "score").text.toInt
    val hand = HandDeserializer.fromXml((xml \ "Hand").head.asInstanceOf[Elem])
    val prediction = (xml \ "prediction").text match {
      case "" => None
      case value => Some(value.toInt)
    }
    val active = (xml \ "active").text.toBoolean
    val player = PlayerFactory.create(id, name).setHand(hand).setScore(score).setActive(active)
    if (prediction.isDefined) player.setPrediction(prediction.get) else player
  }

  override def fromJson(json: JsObject): IPlayer = {
    val id = UUID.fromString((json \ "id").as[String])
    val name = (json \ "name").as[String]
    val profilePicUrl = (json \ "profilePicUrl").as[String]
    val score = (json \ "score").as[Int]
    val hand = HandDeserializer.fromJson((json \ "hand").as[JsObject])
    val prediction = (json \ "prediction").asOpt[Int]
    val active = (json \ "active").as[Boolean]
    val player = PlayerFactory.create(id, name).setHand(hand).setScore(score).setActive(active)
    if (prediction.isDefined) player.setPrediction(prediction.get) else player
  }
}

trait IPlayer extends Serializable{
  val id: UUID
  val name: String
  val profilePicUrl: String
  val hand: IHand
  val score: Int
  val prediction: Option[Int]
  val active: Boolean

  override def toXml: Elem = {
    <Player>
      <id>{id}</id>
      <name>{name}</name>
      <profilePicUrl>{profilePicUrl}</profilePicUrl>
      <score>{score}</score>
      {hand.toXml}
      {prediction match {
        case Some(value) => <prediction>{value}</prediction>
        case None => <prediction></prediction>
      }}
      <active>{active}</active>
    </Player>
  }
  
  override def toJson: JsObject = {
    Json.obj(
      "id" -> id,
      "name" -> name,
      "profilePicUrl" -> profilePicUrl,
      "score" -> score,
      "hand" -> hand.toJson,
      "prediction" -> prediction,
      "active" -> active
    )
  }
  
  def resetHand: IPlayer
  
  def resetPrediction: IPlayer
  
  def playCard(card: ICard): (ICard, IPlayer)
  
  def setHand(hand: IHand): IPlayer
  
  def setPrediction(prediction: Int): IPlayer
  
  def setScore(score: Int): IPlayer
  
  def setActive(active: Boolean): IPlayer
  
  def setUUID(uuid: UUID): IPlayer
  
  def setProfilePicUrl(url: String): IPlayer
}

trait IPlayerFactory {
  def create(uuid: UUID, name: String): IPlayer
}
