import type {PlayerInterface} from "@/model/Player";
import type {JsonRepresentation} from "@/model/Serializable";
import type {TrickInterface} from "@/model/Trick";
import type {DeckInterface} from "@/model/Deck";

export interface GameStateInterface {
    round: number;
    phase: string;
    playerLimit: number;
    roundLimit: number;
    players: PlayerInterface[];
    deck: DeckInterface;
    tricks: TrickInterface[];
}

export class GameState implements GameStateInterface {
  public readonly round: number;
  public readonly phase: string;
  public readonly playerLimit: number;
  public readonly roundLimit: number;
  public readonly players: PlayerInterface[];
  public readonly deck: DeckInterface;
  public readonly tricks: TrickInterface[];

  constructor({round, phase, playerLimit, roundLimit, players, deck, tricks}: JsonRepresentation<GameStateInterface>) {
    this.round = round;
    this.phase = phase;
    this.playerLimit = playerLimit;
    this.roundLimit = roundLimit;
    this.players = players;
    this.deck = deck;
    this.tricks = tricks;
  }

  toJSON(): JsonRepresentation<GameStateInterface> {
    return {
      round: this.round,
      phase: this.phase,
      playerLimit: this.playerLimit,
      roundLimit: this.roundLimit,
      players: this.players,
      deck: this.deck,
      tricks: this.tricks
    };
  }
}
