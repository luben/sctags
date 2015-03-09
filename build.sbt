name := "sctags"

scalaVersion := "2.11.6"

version := "1.0"

scalacOptions ++= Seq("-feature","-deprecation")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler"  % scalaVersion.value
)
