import type {PlayerInterface} from "@/model/Player";
import type {JsonRepresentation} from "@/model/Serializable";

export interface TrickInterface {
  stack: TrickElementInterface[];
}

export interface TrickElementInterface {
  card: CardInterface;
  player: PlayerInterface;
}

export class TrickElement implements TrickElementInterface {
  readonly card: CardInterface
  readonly player: PlayerInterface

  constructor({card, player}: JsonRepresentation<TrickElementInterface>) {
    this.card = card;
    this.player = player
  }

  toJSON(): JsonRepresentation<TrickElementInterface> {
    return {
      card: this.card,
      player: this.player,
    }
  }
}

export class Trick implements TrickInterface {
  readonly stack: TrickElementInterface[]

  constructor({stack}:JsonRepresentation<TrickInterface>) {
    this.stack = stack;
  }

  toJSON(): JsonRepresentation<TrickInterface> {
    return {
      stack: this.stack,
    }
  }
}
