interface DeckInterface {
    cards: CardInterface[];

    toJSON(): object;
}

class Deck implements DeckInterface {
    public readonly cards: CardInterface[];

    constructor(cards: CardInterface[]) {
        this.cards = cards;
    }

    toJSON(): object {
        return {
            cards: this.cards
        };
    }
}
