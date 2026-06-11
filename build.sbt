lazy val root = (project in file("."))
  .settings(
    name := "vars-and-env",
    scalaVersion := "3.3.7",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
