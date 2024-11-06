import type {JsonRepresentation} from "@/model/Serializable";

export interface PlayerInterface {
    name: string;
    hand: HandInterface;
    score: number;
    prediction: number | null;
    active: boolean;
}

export interface HandInterface {
    cards: CardInterface[];
}

export class Hand implements HandInterface {
  readonly cards: CardInterface[];

  constructor({cards}: JsonRepresentation<HandInterface>) {
    this.cards = cards;
  }

  toJSON(): JsonRepresentation<HandInterface> {
    return {
      cards: this.cards,
    }
  }
}

export class Player implements PlayerInterface {
  readonly name: string;
  readonly hand: HandInterface;
  readonly score: number;
  readonly prediction: number | null;
  readonly active: boolean;

  constructor({name, hand, score, prediction, active}: JsonRepresentation<PlayerInterface>) {
    this.name = name;
    this.hand = hand;
    this.score = score;
    this.prediction = prediction;
    this.active = active;
  }

  toJSON(): JsonRepresentation<PlayerInterface> {
    return {
      name: this.name,
      hand: this.hand,
      score: this.score,
      prediction: this.prediction,
      active: this.active,
    };
  }
}
