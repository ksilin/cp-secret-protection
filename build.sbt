// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `cp-secret-protection` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
          library.kafka,
          library.clientPlugins,
          library.scalaLogging,
          library.logback,
          library.oslib,
          library.munit           % Test,
          library.munitScalaCheck % Test
        )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val munit   = "0.7.8"
      val kafka   = "5.5.0-ce"
      val logging = "3.9.2"
      val logback = "1.2.3"
      val oslib   = "0.6.2"
    }
    val kafka           = "org.apache.kafka"           %% "kafka"                % Version.kafka
    val clientPlugins   = "io.confluent"                % "kafka-client-plugins" % Version.kafka
    val oslib           = "com.lihaoyi"                %% "os-lib"               % Version.oslib
    val scalaLogging    = "com.typesafe.scala-logging" %% "scala-logging"        % Version.logging
    val logback         = "ch.qos.logback"              % "logback-classic"      % Version.logback
    val munit           = "org.scalameta"              %% "munit"                % Version.munit
    val munitScalaCheck = "org.scalameta"              %% "munit-scalacheck"     % Version.munit
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val commonSettings =
  Seq(
    scalaVersion := "2.12.11",
    organization := "default",
    organizationName := "konstantin.silin",
    startYear := Some(2020),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-encoding",
        "UTF-8",
        "-Ypartial-unification",
        "-Yrangepos",
        "-Ywarn-unused:imports"
      ),
    javacOptions ++= Seq(
        "-source",
        "1.9",
        "-target",
        "1.9"
      ),
    testFrameworks += new TestFramework("munit.Framework"),
    scalafmtOnCompile := true,
    resolvers += "confluent" at "https://packages.confluent.io/maven/"
  )
fork in Test := true
envVars in Test := Map(
  "CONFLUENT_SECURITY_MASTER_KEY" -> "xWzPxp0QZ013KYneyhOREjYN+RMzxeJfVBW5sUlHiGo="
)
