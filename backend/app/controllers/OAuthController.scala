package controllers

import play.api.mvc.{BaseController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import sttp.client4.circe.*
import io.circe.*
import io.circe.generic.semiauto.*
import sttp.client3.UriContext
import sttp.model.Uri
import sttp.client4.{DefaultSyncBackend, basicRequest}

import javax.inject.Inject


class OAuthController @Inject() (val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  private val clientId = sys.env("CLIENT_ID")
  private val clientSecret = sys.env("CLIENT_SECRET")
  private val redirectUri = sys.env("REDIRECT_URI")

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
    Future.successful(Redirect("https://skullking.it-heimnet.de"))
  }

  def getUser = Action {
    Ok("Hello")
  }
}