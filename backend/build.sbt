import sbt.Keys.libraryDependencies

val scala3Version = "3.5.1"

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
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
    )),
  ).enablePlugins(PlayScala)