import com.typesafe.sbt.packager.docker._

ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "com.thiefspin"
ThisBuild / organizationName := "thiefspin"
ThisBuild / scalacOptions ++= Seq(
  "-Ywarn-dead-code",
  "-Xlint:inaccessible",
  "-Ywarn-unused",
  "-Xlint:unused",
  "-feature",
  "-deprecation",
  "-opt:redundant-casts",
  "-opt:copy-propagation",
  "-unchecked"
)

lazy val akkaVersion = "2.6.5"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-solution",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % "10.1.11",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalaz" %% "scalaz-core" % "7.2.29",
      "org.playframework.anorm" %% "anorm" % "2.6.5",
      "org.scalikejdbc" %% "scalikejdbc" % "3.4.1",
      "org.scalikejdbc" %% "scalikejdbc-config" % "3.4.1",
      "org.postgresql" % "postgresql" % "42.2.5",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "com.opentable.components" % "otj-pg-embedded" % "0.13.3" % Test,
      "org.scoverage" %% "scalac-scoverage-runtime" % "1.4.0" % Test
    ),
    mainClass in(Compile, run) := Some("com.thiefspin.Server"),
    parallelExecution in Test := false,
    javaOptions in Universal ++= Seq("-Dpidfile.path=/dev/null"),
    dockerBaseImage := "openjdk:11-jre-slim",
    dockerCommands ++= Seq(
      ExecCmd("RUN",
        "chmod", "u+x",
        s"${(defaultLinuxInstallLocation in Docker).value}/bin/${executableScriptName.value}")
    ),
    daemonUserUid in Docker := None,
    daemonUser in Docker := "daemon",
    mappings in Docker += file("startup.sh") -> s"${(defaultLinuxInstallLocation in Docker).value}/bin/startup.sh",
    dockerExposedVolumes := Seq("/var/log/scala-solution"),
    dockerExposedPorts := Seq(8080),
    dockerAliases ++= Seq(dockerAlias.value.withTag(Option("latest"))),
    dockerEntrypoint := Seq("sh", "/opt/docker/bin/startup.sh")
  )
