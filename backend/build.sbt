import sbt.Keys.libraryDependencies

val scala3Version = "3.5.1"

val circeVersion = "0.14.10"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val root = project
  .in(file("."))
  .settings(
    name := "Skull King",
    scalaVersion := scala3Version,

    coverageExcludedPackages := "de\\.htwg\\.se\\.skullking\\.SkullKing",

    libraryDependencies ++= (Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.scalactic" %% "scalactic" % "3.2.18",
      "org.scalatest" %% "scalatest" % "3.2.18" % "test",
      guice,
      "net.codingwell" %% "scala-guice" % "6.0.0",
      "com.google.inject" % "guice" % "6.0.0",
      "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
      "com.typesafe.play" %% "play-json" % "2.10.5",
      "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M24",
      "com.softwaremill.sttp.client4" %% "upickle" % "4.0.0-M24",
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.11.13",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.11.13",
      "com.softwaremill.sttp.client4" %% "circe" % "4.0.0-M24",


      "org.polyvariant" %% "sttp-oauth2" % "0.19.2",
      "org.polyvariant" %% "sttp-oauth2-circe" % "0.19.2"
    )),
  ).enablePlugins(PlayScala)