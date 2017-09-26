name := "sctags"

scalaVersion := "2.12.3"

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
    """CMD=$0""",
    """ARGS=()""",
    """for opt in "$@"; do""",
    """  case $opt in """,
    """  --version)""",
    """    echo 'Exuberant Ctags 5.9~forScala https://github.com/luben/sctags'""",
    """    exit 0""",
    """    ;;""",
    """  --list-languages)""",
    """    echo 'Scala'""",
    """    exec ctags --list-languages""",
    """    ;;""",
    """  --language-force=*)""",
    """    CTAGS_LANG="${opt#*=}"""",
    """    ;;""",
    """  *)""",
    """    ARGS[${#ARGS[@]}]=$opt""",
    """    shift""",
    """    ;;""",
    """  esac""",
    """done""",
    """if [[ -n "$CTAGS_LANG" && "$CTAGS_LANG" != "scala" && "$CTAGS_LANG" != "Scala" ]]; then""",
    """  exec ctags --language-force=$CTAGS_LANG ${ARGS[@]}""",
    """else""",
    """  if [[ -z $SCALA_LIB ]]; then""",
    """    if [[ -n $SCALA_HOME ]]; then""",
    """      SCALA_LIB="$SCALA_HOME/lib"""",
    """    else""",
    """      SCALA_BIN=$(which scala)""",
    """      if [[ $? != 0 ]]; then""",
    """         echo "Could not find scala executable on your path: $PATH"""",
    """         echo "Please set SCALA_HOME or SCALA_LIB environment variable"""",
    """         exit 1;""",
    """      fi""",
    """      if [[ -L "$SCALA_BIN" ]]; then""",
    """        SCALA_BIN=$(readlink $SCALA_BIN)""",
    """      fi""",
    """      SCALA_HOME=$(dirname "$SCALA_BIN")/..""",
    """      SCALA_LIB="$SCALA_HOME/lib"""",
    """    fi""",
    """  fi""",
    """  exec java -Xbootclasspath/"a:$SCALA_LIB/scala-compiler.jar:$SCALA_LIB/scala-library.jar:$SCALA_LIB/scala-reflect.jar" -jar "$CMD" ${ARGS[@]}""",
    """fi"""
  )))
