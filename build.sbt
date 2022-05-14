ThisBuild / organization := "edu.gatech.cs3220.spring2022.motorolla68k"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "root",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"
  )
