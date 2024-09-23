name := """playapi"""
organization := "com.tarento"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.23"


libraryDependencies ++= Seq(
  "junit" % "junit" % "4.13.2" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.mockito" % "mockito-core" % "3.12.4" % Test
)
