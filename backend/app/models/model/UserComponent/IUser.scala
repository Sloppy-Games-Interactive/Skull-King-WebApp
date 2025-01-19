package de.htwg.se.skullking.model.UserComponent

import play.api.libs.json.{JsObject, Json}
import de.htwg.se.skullking.modules.Serializable


import scala.xml.Elem

trait IUser extends Serializable{
  val id: Int
  val name: String
  val email: String
  val avatar_url: String
  val login: String
  
  override def toJson: JsObject = {
    Json.obj(
      "id" -> id,
      "name" -> name,
      "email" -> email,
      "avatar_url" -> avatar_url,
      "login" -> login
    )
  }

  override def toXml: Elem = {
    <User>
      <id>
        {id}
      </id>
      <name>
        {name}
      </name>
      <email>
        {email}
      </email>
      <avatar_url>
        {avatar_url}
      </avatar_url>
      <login>
        {login}
      </login>
    </User>
  }
}
