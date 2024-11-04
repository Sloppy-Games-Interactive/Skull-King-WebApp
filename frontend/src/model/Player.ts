interface PlayerInterface {
    name: string;
    hand: HandInterface;
    score: number;
    prediction: number | null;
    active: boolean;
}

interface HandInterface {
    cards: CardInterface[];
}
