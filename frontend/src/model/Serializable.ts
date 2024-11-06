export type JsonRepresentation<Object> = {
  [Key in keyof Object]: Object[Key]
};

export interface Serializable<T extends object> {
  toJSON(): JsonRepresentation<T>;
  fromJSON(json: JsonRepresentation<T>): JsonRepresentation<T>
}
