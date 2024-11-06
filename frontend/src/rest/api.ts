abstract class BaseApiService {
  private readonly baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  async get(path: string) {
    const response = await fetch(this.baseUrl + path);
    return await response.json();
  }

  async post(path: string, data: any) {
    const response = await fetch(this.baseUrl + path, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });
    return await response.json();
  }

  async put(path: string, data: any) {
    const response = await fetch(this.baseUrl + path, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });
    return await response.json();
  }

  async delete(path: string) {
    const response = await fetch(this.baseUrl + path, {
      method: 'DELETE'
    });
    return await response.json();
  }
}

// TODO add response parsing etc
export class ApiService extends BaseApiService {
  constructor() {
    super('localhost:9000');
  }

  async getStatus() {
    const state = await this.get('/status');
  }

  async newGame() {
    return await this.post('/new-game', {});
  }

  async setPlayerLimit(playerLimit: number) {
    return await this.post('/set-player-limit', { playerLimit });
  }

  async setPlayerName(playerName: string) {
    return await this.post('/set-player-name', { playerName });
  }

  async setPrediction(prediction: number) {
    return await this.post('/set-prediction', { prediction });
  }

  async playCard(card: CardInterface) {
    return await this.post('/play-card', { card });
  }

  async saveGame() {
    return await this.post('/save-game', {});
  }

  async loadGame() {
    return await this.post('/load-game', {});
  }
}
