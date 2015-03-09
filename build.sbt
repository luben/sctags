name := "sctags"

scalaVersion := "2.11.6"

version := "1.0"

scalacOptions ++= Seq("-feature","-deprecation")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler"  % scalaVersion.value
)

assemblyJarName in assembly := name.value

mainClass in assembly := Some("sctags.SCTags")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false, includeDependency = false)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(Seq(
    """#!/bin/bash""",
    """if [[ -z $SCALA_LIB ]]; then""",
    """ SCALA_BIN=$(which scala)""",
    """ SCALA_HOME=$(dirname $SCALA_BIN)""",
    """ SCALA_LIB="$SCALA_HOME/../lib"""",
    """fi""",
    """exec java -Xbootclasspath/a:$SCALA_LIB/scala-compiler.jar:$SCALA_LIB/scala-library.jar:$SCALA_LIB/scala-reflect.jar -jar "$0" "$@""""
  )))
