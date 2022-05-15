ThisBuild / organization := "edu.gatech.cs3220.spring2022.motorolla68k"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "root",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % "3.5.1",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test",
      "edu.berkeley.cs" %% "chiseltest" % "0.5.1" % "test"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-Xcheckinit",
      "-deprecation",
      "-feature",
      "-P:chiselplugin:genBundleElements"
    ),
    addCompilerPlugin(
      "edu.berkeley.cs" % "chisel3-plugin" % "3.5.1" cross CrossVersion.full
    )
  )
