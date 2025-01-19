package de.htwg.se.skullking.model.UserComponent

case class User(
  id: Int,
  name: String,
  email: String,
  avatar_url: String,
  login: String
) extends IUser

