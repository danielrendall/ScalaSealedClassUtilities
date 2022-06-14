lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.15"
lazy val scala213 = "2.13.8"
lazy val supportedScalaVersions = List(scala213/*, scala212, scala211*/)

import Dependencies._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion     := scala213
ThisBuild / organization     := "uk.co.danielrendall"
ThisBuild / organizationName := "scala-sealed-class-utilities"

githubOwner := "danielrendall"
githubRepository := "ScalaSealedClassUtilities"
githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")
releaseCrossBuild := true

lazy val root = (project in file("."))
  .settings(
    name := "sealed-class-utilities",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      specs2 % Test
    )
  )
