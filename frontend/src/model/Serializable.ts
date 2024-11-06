type JsonRepresentation<Object> = {
  [Key in keyof Object]: Object[Key]
}

export interface Serializable<T> {
  toJSON(): SerializableJson<T>
}

export type SerializableJson<T> = Omit<JsonRepresentation<T>, 'toJSON'>
