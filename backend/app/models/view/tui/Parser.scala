package de.htwg.se.skullking.view.tui

import de.htwg.se.skullking.model.CardComponent.{ICard, CardDeserializer}
import de.htwg.se.skullking.model.PlayerComponent.IPlayer

import play.api.libs.json.{JsObject, Json}
import scala.util.{Success, Try}

class Parser {
  def parsePlayerLimit(input: Int): Option[Int] = {

    input match {
      case playerLimit if playerLimit >= 2 && playerLimit <= 9 => Some(playerLimit)
      case _ => {
        println("Player count must be a number between 2 and 9.")
        None
      }
    }
  }

  def parsePlayerName(input: String): Option[String] = {
    val tryName = Try(input.trim)

    tryName match {
      case Success(name) if name.nonEmpty => Some(name)
      case _ => {
        println("Player name must not be empty.")
        None
      }
    }
  }

  def parsePrediction(input: Int, round: Int): Option[Int] = {

    input match {
      case prediction if prediction >= 0 && prediction <= round => Some(prediction)
      case _ => {
        println(s"Prediction must be a number between 0 and ${round}.")
        None
      }
    }
  }

  def parseCardPlay(input: JsObject): Option[ICard] = {
    Try(CardDeserializer.fromJson(input)) match {
      case Success(card) => Some(card)
      case _ => {
        println("Invalid card.")
        None
      }
    }
  }
}
