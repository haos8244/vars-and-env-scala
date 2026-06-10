lazy val root = (project in file("."))
  .settings(
    name := "vars-and-env",
    scalaVersion := "3.8.4",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
