import type {JsonRepresentation} from "@/model/Serializable";

export interface DeckInterface {
    cards: CardInterface[];
}

export class Deck implements DeckInterface {
    readonly cards: CardInterface[];

    constructor({cards}: JsonRepresentation<DeckInterface>) {
        this.cards = cards;
    }

    toJSON(): JsonRepresentation<DeckInterface> {
        return {
            cards: this.cards
        };
    }
}
