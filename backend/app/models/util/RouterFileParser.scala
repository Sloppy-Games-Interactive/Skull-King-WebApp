package de.htwg.se.skullking.util

import scala.io.Source

case class RouteInfo(method: String, path: String, description: String)

object RoutesUtil {
  def getRoutes: List[RouteInfo] = {
    val source = Source.fromFile("conf/routes")
    val lines = source.getLines().toList
    source.close()

    var lastDescription = ""
    var skipNextRoute = false
    val routes = lines.collect {
      case line if line.startsWith("##") =>
        lastDescription = line.replace("##", "").trim
        None
      case line if line.startsWith("#-- skip") =>
        skipNextRoute = true
        None
      case line if line.matches("^(GET|POST|PUT|DELETE|PATCH)\\s+/.*") =>
        if (skipNextRoute) {
          skipNextRoute = false
          None
        } else {
          val parts = line.split("\\s+")
          val route = RouteInfo(parts(0), parts(1), lastDescription)
          lastDescription = "" // Reset the description after processing the route
          Some(route)
        }
      case _ => None
    }.flatten

    routes
  }
}

