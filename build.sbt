ThisBuild / scalaVersion := "2.13.10"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """recette-api""",
    libraryDependencies ++= Seq(
      guice,
      "org.mongodb.scala" %% "mongo-scala-driver" % "4.8.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    )
  )