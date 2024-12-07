package de.htwg.se.skullking.model.PlayerComponent.PlayerBaseImpl

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.PlayerComponent.*

import java.util.UUID

case class Player(
  id: Int = 0,
  name: String,
  // TODO: change default profile pic
  profilePicUrl: String = "https://api.dicebear.com/9.x/bottts/svg",
  // maybe the server should generate the UUID and send it to the client not the other way around
  uuid: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),
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

  def setUUID(uuid: UUID): IPlayer = this.copy(uuid = uuid)

  def setProfilePicUrl(url: String): IPlayer = this.copy(profilePicUrl = url)
  
  override def toString: String = s"$name: $score, $hand, prediction: ${prediction.getOrElse("-")}"
}

object PlayerFactory extends IPlayerFactory {
  def create(id: Int, name: String): IPlayer = Player(id, name)
} 
