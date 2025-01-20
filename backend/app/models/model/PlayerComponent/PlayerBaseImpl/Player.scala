package de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.PlayerComponent.*
import scala.util.Random


import java.util.UUID

case class Player(
  id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),
  name: String,
  profilePic: String = s"${Random.nextInt(9) + 1}.webp",
  // maybe the server should generate the UUID and send it to the client not the other way around
  hand: IHand = Hand(),
  score: Int = 0,
  prediction: Option[Int] = None,
  active: Boolean = false
) extends IPlayer {
  def resetHand: IPlayer = this.copy(hand = Hand())
  
  def resetPrediction: IPlayer = this.copy(prediction = None)
  
  def playCard(card: ICard): (ICard, IPlayer) = {
    hand.indexOf(card) match {
      case Some(idx) => {
        val (card, newHand) = hand.play(idx)
        (card, this.copy(hand = newHand))
      }
      case None => (card, this)
    }
  }
  
  def setHand(hand: IHand): IPlayer = this.copy(hand = hand)
  
  def setPrediction(prediction: Int): IPlayer = this.copy(prediction = Some(prediction))
  
  def setScore(score: Int): IPlayer = this.copy(score = score)
  
  def setActive(active: Boolean): IPlayer = this.copy(active = active)

  def setUUID(uuid: UUID): IPlayer = this.copy(id = uuid)

  def setProfilePic(url: String): IPlayer = this.copy(profilePic = url)
  
  override def toString: String = s"$name: $score, $hand, prediction: ${prediction.getOrElse("-")}"
}

object PlayerFactory extends IPlayerFactory {
  def create(id: UUID, name: String): IPlayer = Player(id, name)
} 
