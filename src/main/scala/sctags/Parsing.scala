package sctags

import scala.tools.nsc.{Global, CompilationUnits}
import scala.tools.nsc.io.AbstractFile
import scala.tools.nsc.ast.Trees
import scala.tools.nsc.ast.parser.SyntaxAnalyzer
import scala.reflect.internal.util.BatchSourceFile

import java.io.File;

trait Parsing { this: SCTags.type =>
  import compiler.syntaxAnalyzer._
  import compiler._

  def parse(af: AbstractFile): Tree =
    parse(new CompilationUnit(new BatchSourceFile(af)))

  def parse(f: File): Tree =
    parse(AbstractFile.getFile(f))

  def parse(fname: String): Tree =
    parse(AbstractFile.getFile(fname))

  def parse(cu: CompilationUnit): Tree = {
    new compiler.Run
    val parser = new UnitParser(cu)
    val tree = parser.compilationUnit
    compiler.analyzer.newNamer(compiler.analyzer.rootContext(cu, tree, false)).enterSym(tree)
    tree
  }
}
