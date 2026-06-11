ThisBuild / scalaVersion := "3.3.7"

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

lazy val problem1    = (project in file("scala-code/problem1")).settings(commonSettings)
lazy val problem2    = (project in file("scala-code/problem2")).settings(commonSettings)
lazy val problem3    = (project in file("scala-code/problem3")).settings(commonSettings)
lazy val problem4    = (project in file("scala-code/problem4")).settings(commonSettings)
lazy val problem5    = (project in file("scala-code/problem5")).settings(commonSettings)
lazy val problem6    = (project in file("scala-code/problem6")).settings(commonSettings)
lazy val problem7    = (project in file("scala-code/problem7")).settings(commonSettings)
lazy val problem8    = (project in file("scala-code/problem8")).settings(commonSettings)
lazy val capstone    = (project in file("scala-code/capstone")).settings(commonSettings)
lazy val mapexercise = (project in file("scala-code/mapexercise")).settings(commonSettings)

lazy val root = (project in file("."))
  .aggregate(
    problem1, problem2, problem3, problem4, problem5,
    problem6, problem7, problem8, capstone, mapexercise
  )
  .settings(name := "vars-and-env")
