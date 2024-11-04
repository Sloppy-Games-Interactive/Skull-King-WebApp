interface CardInterface {
  suit: string;
  isSpecial: boolean;
  isTrump: boolean;
}

// TODO TODO TODO
interface StandardCardInterface extends CardInterface {
  value: number;
}

interface SpecialCardInterface extends CardInterface {}
interface JokerCardInterface extends SpecialCardInterface {}
