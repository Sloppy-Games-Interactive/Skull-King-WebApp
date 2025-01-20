package controllers

import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}
import sttp.client4.circe.*
import io.circe.*
import io.circe.generic.semiauto.*
import de.htwg.se.skullking.model.UserComponent.User
import play.api.libs.json.JsValue
import sttp.client3.UriContext
import sttp.model.Uri
import sttp.client4.{DefaultSyncBackend, basicRequest}
import play.api.libs.json.Json

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable


object OAuthSession {
  private val session: mutable.Map[UUID, JsValue] = mutable.Map()

  def getSession(uuid: UUID): Option[JsValue] = {
    session.get(uuid)
  }

  def setSession(u: UUID, s: JsValue): Unit = {
    session.update(u, s)
  }
}

class OAuthController @Inject() (val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  private val clientId = sys.env("CLIENT_ID")
  private val clientSecret = sys.env("CLIENT_SECRET")
  private val redirectUri = sys.env("REDIRECT_URI")

  // Maybe async makes problems
  def callback = Action.async { implicit request =>
    val code = request.getQueryString("code").getOrElse("")
    val state = request.getQueryString("state").getOrElse("")

    val authCode = code
    case class MyTokenResponse(access_token: Option[String], scope: Option[String], token_type: String, refresh_token: Option[String])
    implicit val tokenResponseDecoder: Decoder[MyTokenResponse] = deriveDecoder[MyTokenResponse]
    val backend = DefaultSyncBackend()

    val tokenRequest = basicRequest
      .post(uri"https://github.com/login/oauth/access_token?code=$authCode&grant_type=authorization_code")
      .auth
      .basic(clientId, clientSecret)
      .header("accept", "application/json")
    val authResponse = tokenRequest.response(asJson[MyTokenResponse]).send(backend)
    val accessToken = authResponse.body.map(_.access_token)

    accessToken match {
      case Right(Some(token)) => {
        val uuid = getUserData(token)
        Future.successful(Redirect("https://skullking.it-heimnet.de/?uuid=" + uuid.toString))
      }
      case Right(None) => Future.successful(InternalServerError("No access token"))
      case Left(error) => Future.successful(InternalServerError(error.getMessage))
    }
  }

  def getUserData(token: String): UUID =  {
    val backend = DefaultSyncBackend()
    val userDataRequest = basicRequest
      .header("Authorization", s"Bearer ${token}")
      .get(uri"https://api.github.com/user")

    val userDataResponse = userDataRequest.send(backend)
    val userData = userDataResponse.body match {
      case Right(data) => data
      case Left(error) => throw new Exception(error)
    }
    val uuid = UUID.randomUUID()

    OAuthSession.setSession(uuid, Json.parse(userData))
    uuid
  }

  def getSession = Action { implicit request: Request[AnyContent] =>
    val uuid = request.body.asJson.get("sessionUuid").as[UUID]
    OAuthSession.getSession(uuid) match {
      case Some(session) => {
        val user = User(id = (session \ "id").as[Int], name = (session \ "name").as[String], email = (session \ "email").as[String], avatar_url = (session \ "avatar_url").as[String], login = (session \ "login").as[String])

        Ok(user.toJson)
      }
      case None => NotFound("Session not found")
    }
  }
}