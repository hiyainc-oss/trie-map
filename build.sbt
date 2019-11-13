import sbt.Keys.libraryDependencies

ThisBuild / organization := "com.hiya"
ThisBuild / scalacOptions ++= Seq("-Xfatal-warnings", "-Xlint","-deprecation","-feature","-unchecked")
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.1")

lazy val `trie-map` = (project in file("."))
    .settings(
      libraryDependencies ++= Seq(
        "org.scalactic" %% "scalactic" % "3.0.8",
        "org.scalatest" %% "scalatest" % "3.0.8" % Test
      )
    )


