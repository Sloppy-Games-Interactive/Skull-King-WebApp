import type {JsonRepresentation} from "@/model/Serializable";

export interface CardInterface {
  suit: string;
  isSpecial: boolean;
  isTrump: boolean;
}

// TODO TODO TODO
export interface StandardCardInterface extends CardInterface {
  rank: number
}

export interface SpecialCardInterface extends CardInterface {

}

export interface JokerCardInterface extends SpecialCardInterface {}

class Card implements CardInterface {
  readonly suit: string;
  readonly isSpecial: boolean;
  readonly isTrump: boolean;

  constructor({suit, isSpecial, isTrump}: JsonRepresentation<CardInterface>) {
    this.suit = suit;
    this.isSpecial = isSpecial;
    this.isTrump = isTrump;
  }

  toJSON(): JsonRepresentation<CardInterface> {
    return {
      suit: this.suit,
      isSpecial: this.isSpecial,
      isTrump: this.isTrump
    };
  }
}

export class StandardCard extends Card implements StandardCardInterface {
  readonly rank: number;

  constructor({suit, rank, isSpecial, isTrump}: JsonRepresentation<StandardCardInterface>) {
    super({suit, isSpecial, isTrump})
    this.rank = rank;
  }

  toJSON(): JsonRepresentation<StandardCardInterface> {
    return {
      rank: this.rank,
      ...super.toJSON(),
    }
  }
}
