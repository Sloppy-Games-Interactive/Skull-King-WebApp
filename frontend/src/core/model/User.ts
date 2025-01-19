import type { Serializable, SerializableJson } from '@/core/model/Serializable'

export interface UserInterface extends Serializable<UserInterface> {
  name: string
  email: string
  avatar_url: string
  login: string
  id: number
}

export class User implements UserInterface {
  readonly name: string
  readonly email: string
  readonly avatar_url: string
  readonly login: string
  readonly id: number

  constructor({ name, email, avatar_url, login, id }: SerializableJson<UserInterface>) {
    this.name = name
    this.email = email
    this.avatar_url = avatar_url
    this.login = login
    this.id = id
  }
  toJSON(): SerializableJson<UserInterface> {
    return {
      name: this.name,
      email: this.email,
      avatar_url: this.avatar_url,
      login: this.login,
      id: this.id
    }
  }
}
