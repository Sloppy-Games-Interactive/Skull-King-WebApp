package de.htwg.se.skullking.model.CardComponent.CardBaseImpl

import de.htwg.se.skullking.model.CardComponent.*
import scala.util.{Failure, Success, Try}

abstract class Card extends ICard {
  def isSpecial: Boolean = suit.cardType match {
    case CardType.Special => true
    case _ => false
  }

  def isTrump: Boolean = suit.cardType match {
    case CardType.Trump => true
    case _ => false
  }
}

case class StandardCard(
  suit: Suit,
  value: Int
) extends Card with IStandardCard {
  override def equals (that: Any): Boolean = that match {
    case s: IStandardCard => s.suit == suit && s.value == value
    case _ => false
  }
  override def toString: String = s"${suit.readable} $value"
}

class SpecialCard(val suit: Suit) extends Card with ISpecialCard {
  override def equals (that: Any): Boolean = that match {
    case s: ISpecialCard => s.suit == suit
    case _ => false
  }
  override def toString: String = s"${suit.readable}"
}

case class JokerCard(as: JokerBehaviour = JokerBehaviour.None) extends SpecialCard(Suit.Joker) with IJokerCard {
  override def equals (that: Any): Boolean = that match {
    case s: IJokerCard => s.suit == suit && s.as == as
    case _ => false
  }
  override def toString: String = s"${super.toString} as ${as.readable}"
  def playAs(behaviour: JokerBehaviour): JokerCard = JokerCard(behaviour)
}

object CardFactory extends ICardFactory {
  def apply(suit: Suit, value: Int): StandardCard = suit match {
    case s: Suit => StandardCard(s, value)
  }
  
  def apply(suit: Suit): SpecialCard = suit match {
    case s: Suit if s == Suit.Joker => JokerCard()
    case s: Suit => SpecialCard(s)
  }
  
  def apply(jokerBehaviour: JokerBehaviour): JokerCard = JokerCard(jokerBehaviour)

  def apply(card: String): Option[ICard] = {
    val parts = card.split(" ")

    if (parts.length > 0) {
      Suit.withName(parts(0)) match {
        case Some(suit) if (parts.length == 2) =>
          Try(parts(1).toInt) match {
            case Success(value) => Some(apply(suit, value))
            case _ => None
          }
        case Some(suit) if (parts.length == 1) => Some(apply(suit))
        case Some(suit) if (suit == Suit.Joker && parts(1) == "as") =>
          val as = JokerBehaviour.withName(parts(2))
          Some(apply(as))
        case _ => None
      }
    } else {
      None
    }
  }
}
