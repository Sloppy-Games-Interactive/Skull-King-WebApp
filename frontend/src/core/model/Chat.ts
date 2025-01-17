import type { PlayerInterface } from '@/core/model/Player'
import type { Serializable, SerializableJson } from '@/core/model/Serializable'
import { Player } from '@/core/model/Player'

export interface ChatMessageInterface
  extends Serializable<ChatMessageInterface> {
  author: PlayerInterface
  message: string
}

export class ChatMessage implements ChatMessageInterface {
  readonly author: PlayerInterface
  readonly message: string

  constructor({ author, message }: SerializableJson<ChatMessageInterface>) {
    this.author = new Player(author)
    this.message = message
  }

  toJSON(): SerializableJson<ChatMessageInterface> {
    return {
      author: this.author,
      message: this.message,
    }
  }
}
