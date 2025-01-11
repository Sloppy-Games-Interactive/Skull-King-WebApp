package de.htwg.se.skullking.model.StateComponent.GameStateBaseImpl

import de.htwg.se.skullking.model.CardComponent.ICard
import de.htwg.se.skullking.model.DeckComponent.{DeckContent, IDeck, IDeckFactory}
import de.htwg.se.skullking.model.PlayerComponent.IPlayer
import de.htwg.se.skullking.model.StateComponent.*
import de.htwg.se.skullking.model.trick.TrickComponent.ITrick
import de.htwg.se.skullking.modules.Default.given

case class GameState(
  phase: Phase = Phase.PrepareGame,
  playerLimit: Int = 0,
  players: List[IPlayer] = List(),
  round: Int = 0,
  tricks: List[ITrick] = List(),
  deck: IDeck = summon[IDeck],
  roundLimit: Int = 10,
  lastTrickWinner: Option[IPlayer] = None
) extends IGameState {
  def handleEvent(event: GameStateEvent): IGameState = event match {
    case SetPlayerLimitEvent(n) if phase == Phase.PrepareGame && (players.length <= n) => setPlayerLimit(n)
    case AddPlayerEvent(player) if phase == Phase.PrepareGame && playerLimit > 0 => addPlayer(player)
    case SetPredictionEvent(player, prediction) if phase == Phase.PrepareTricks => setPrediction(player, prediction)
    case PlayCardEvent(player, card) if phase == Phase.PlayTricks => playCard(player, card)
    case StartGameEvent() if phase == Phase.PrepareGame => startGame
    case _ => this
  }

  def activePlayer: Option[IPlayer] = players.find(_.active)

  def activeTrick: Option[ITrick] = tricks.headOption

  private def startGame: GameState = {
    if (players.length == playerLimit && playerLimit > 1) {
      prepareRound
    } else {
      this
    }
  }

  private def changePhase(nextPhase: Phase): GameState = this.copy(phase = nextPhase)

  private def setPlayerLimit(n: Int): GameState = this.copy(playerLimit = n)

  def addPlayer(player: IPlayer): GameState = {
    if (players.length == playerLimit) {
      this
    } else {
      this.copy(players = players :+ player)
    }
  }

  private def prepareRound: GameState = {
    val updatedPlayers = players.map(_.resetHand.resetPrediction.setActive(false))

    this.copy(
        round = round + 1,
        deck = summon[IDeckFactory](DeckContent.specials).shuffle(),
        players = updatedPlayers.head.setActive(true) :: updatedPlayers.tail
      ).dealCards
      .changePhase(Phase.PrepareTricks)
  }

  private def dealCards: GameState = {
    val (newDeck, updatedPlayers) = players.foldLeft((deck, List[IPlayer]())) {
      case ((currentDeck, playerList), player) =>
        val (newDeck, newHand) = player.hand.drawFromDeck(currentDeck, round)
        (newDeck, player.setHand(newHand) :: playerList)
    }

    this.copy(players = updatedPlayers.reverse, deck = newDeck)
  }

  private def setPrediction(player: IPlayer, newPrediction: Int): GameState = {
    val updatedPlayers = players.map {
      case p if p.id == player.id => p.setPrediction(newPrediction)
      case p => p
    }

    if updatedPlayers.forall(_.prediction.isDefined) then
      this.copy(players = determineRoundStartPlayer(updatedPlayers, round)).startTrick
    else
      this.copy(players = updatedPlayers)
  }

  private def startTrick: GameState = {
    val lastWinner = activeTrick.flatMap(_.winner)
    
    this.copy(
      lastTrickWinner = lastWinner,
      tricks = summon[ITrick] :: tricks,
      players = determineTrickStartPlayer(players, round, lastWinner)
    ).changePhase(Phase.PlayTricks)
  }

  private def playCard(player: IPlayer, card: ICard): GameState = {
    val nextState = {
      for {
        _ <- players.find(_ == player)
        trick <- tricks.headOption
      } yield {
        val (playedCard, updatedPlayer) = player.playCard(card)
        val updatedTrick = trick.play(playedCard, updatedPlayer)
        val updatedPlayers = players.map {
          case p if p.id == updatedPlayer.id => updatedPlayer
          case p => p
        }

        this.copy(
          tricks = tricks.updated(0, updatedTrick),
          players = rotateToNextActivePlayer(updatedPlayers)
        )
      }
    }.getOrElse(this)

    nextState.activeTrick match {
      case Some(trick) if trick.players.length == players.length => nextState.endTrick
      case _ => nextState
    }
  }

  private def endTrick: GameState = {
    if (tricks.length == round) {
      endRound
    } else {
      startTrick
    }
  }

  private def endRound: GameState = {
    val updatedPlayers = players.map { player =>
      val wonTricks = for {
        trick <- tricks
        winner <- trick.winner if winner.id == player.id
      } yield trick

      val numTricksWon = wonTricks.length

      player.prediction.fold(player) { prediction =>
        val scoreChange = prediction match {
          case 0 if numTricksWon == 0 => 10 * round
          case 0 => -10 * round
          case _ if prediction == numTricksWon => 20 * numTricksWon + wonTricks.map(_.calculateBonusPoints).sum
          case _ => -10 * Math.abs(numTricksWon - prediction)
        }
        player.setScore(player.score + scoreChange)
      }
    }

    val lastWinner = activeTrick.flatMap(_.winner)

    val nextState = this.copy(
      players = updatedPlayers,
      tricks = List(),
      lastTrickWinner = lastWinner
    )

    if (round == roundLimit) {
      nextState.changePhase(Phase.EndGame)
    } else {
      nextState.prepareRound
    }
  }

  private def determineRoundStartPlayer(players: List[IPlayer], roundNumber: Int): List[IPlayer] = {
    val activePlayerIndex = (roundNumber - 1) % players.length
    players.zipWithIndex.map {
      case (p, i) if i == activePlayerIndex => p.setActive(true)
      case (p, _) => p.setActive(false)
    }
  }

  private def determineTrickStartPlayer(players: List[IPlayer], roundNumber: Int, lastWinner: Option[IPlayer]): List[IPlayer] = {
    lastWinner match {
      case Some(winner) => players.map {
        case p if p.id == winner.id => p.setActive(true)
        case p => p.setActive(false)
      }
      case None => determineRoundStartPlayer(players, roundNumber)
    }
  }

  private def rotateToNextActivePlayer(players: List[IPlayer]): List[IPlayer] = {
    if players.last.active then
      players.head.setActive(true) :: players.tail.map(_.setActive(false))
    else
      setNextPlayerActive(players)
  }

  private def setNextPlayerActive(players: List[IPlayer]): List[IPlayer] = {
    val nextPlayer = nextActive(players)
    players.map {
      case p if nextPlayer.isDefined && p.id == nextPlayer.get.id => p.setActive(true)
      case p => p.setActive(false)
    }
  }

  private def nextActive(players: List[IPlayer]): Option[IPlayer] = {
    val nextIndex = players.indexWhere(_.active) + 1
    players.lift(nextIndex)
  }
}


object GameStateFactory extends IGameStateFactory {
  def apply(
    round: Int,
    phase: Phase,
    playerLimit: Int,
    roundLimit: Int,
    deck: IDeck,
    players: List[IPlayer],
    tricks: List[ITrick]
  ): IGameState = GameState(phase, playerLimit, players, round, tricks, deck, roundLimit)
}