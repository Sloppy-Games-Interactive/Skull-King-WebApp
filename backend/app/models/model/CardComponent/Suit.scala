package de.htwg.se.skullking.model.CardComponent

import de.htwg.se.skullking.model.CardComponent.CardType.CardType

enum Suit(val readable: String, val cardType: CardType) {
  case Red extends Suit("🟥", CardType.Standard)
  case Yellow extends Suit("🟡", CardType.Standard)
  case Blue extends Suit("🔷", CardType.Standard)
  case Black extends Suit("☠️", CardType.Trump)
  case Escape extends Suit("🏝️", CardType.Special)
  case SkullKing extends Suit("💀", CardType.Special)
  case Pirate extends Suit("🏴‍☠️", CardType.Special)
  case Mermaid extends Suit("🧜", CardType.Special)
  case Joker extends Suit("🃏", CardType.Special)
}

object Suit {
  def withName(name: String): Option[Suit] = name match {
    case "🟥" => Some(Suit.Red)
    case "🟡" => Some(Suit.Yellow)
    case "🔷" => Some(Suit.Blue)
    case "☠️" => Some(Suit.Black)
    case "🏝️" => Some(Suit.Escape)
    case "💀" => Some(Suit.SkullKing)
    case "🏴‍☠️" => Some(Suit.Pirate)
    case "🧜" => Some(Suit.Mermaid)
    case "🃏" => Some(Suit.Joker)
    case _ => None
  }
}
