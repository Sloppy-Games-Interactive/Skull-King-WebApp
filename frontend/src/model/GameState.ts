interface GameStateInterface {
    round: number;
    phase: string;
    playerLimit: number;
    roundLimit: number;
    players: PlayerInterface[];
    deck: DeckInterface;
    tricks: TrickInterface[];

    toJSON(): object;
}

class GameState implements GameStateInterface {
  public readonly round: number;
  public readonly phase: string;
  public readonly playerLimit: number;
  public readonly roundLimit: number;
  public readonly players: PlayerInterface[];
  public readonly deck: DeckInterface;
  public readonly tricks: TrickInterface[];

  constructor(round: number, phase: string, playerLimit: number, roundLimit: number, players: PlayerInterface[], deck: DeckInterface, tricks: TrickInterface[]) {
    this.round = round;
    this.phase = phase;
    this.playerLimit = playerLimit;
    this.roundLimit = roundLimit;
    this.players = players;
    this.deck = deck;
    this.tricks = tricks;
  }

  toJSON(): object {
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

class GameStateDeserializer {
  static fromJson(json: string): GameStateInterface {
    const obj = JSON.parse(json);
    return new GameState(obj.round, obj.phase, obj.playerLimit, obj.roundLimit, obj.players, obj.deck, obj.tricks);
  }
}
