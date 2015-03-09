package sctags

import scala.tools.nsc.Global

import scala.collection._

trait TagGeneration { this: SCTags.type =>
  import compiler._
  def generateTags(tree: Tree): Seq[Tag] = {
    class GenTagTraverser extends compiler.Traverser {
      val _tags = new mutable.ListBuffer[Tag]
      var path = new mutable.Stack[(String,String)]
      def tags: Seq[Tag] = _tags.toList

      import compiler._

      def addTag(pos: TagPosition, name: Name, fields: Map[String, String]) {
        if (!name.decode.contains('$') && !name.decode.equals("this")) {
          _tags += Tag(name.decode, pos, fields.toList: _*)
        }
      }

      def access(t: Tree) = t match {
        case (m: MemberDef) if m.mods.isPrivate   => Some("private")
        case (m: MemberDef) if m.mods.isProtected => Some("protected")
        case (m: MemberDef) if m.mods.isPublic    => Some("public")
        case _ => None
      }

      def implementation(t: Tree) = t match {
        case (m: MemberDef) if m.mods.isFinal         => Some("final")
        case (m: MemberDef) if m.mods.hasAbstractFlag => Some("abstract")
        case (m: MemberDef) if m.mods.isImplicit      => Some("implicit")
        case (m: MemberDef) if m.mods.isSealed        => Some("sealed")
        case (m: MemberDef) if m.mods.isLazy          => Some("lazy")
        case _ => None
      }

      def kind(t: Tree) = t match {
        case PackageDef(_,_)                => Some("p")
        case ModuleDef(m,_,_)  if m.isCase  => Some("O")
        case ModuleDef(_,_,_)               => Some("o")
        case ClassDef(m,_,_,_) if m.isCase  => Some("C")
        case ClassDef(m,_,_,_) if m.isTrait => Some("t")
        case ClassDef(m,_,_,_)              => Some("c")
        case ValDef(m,_,_,_) if m.isMutable => Some("v")
        case ValDef(_,_,_,_)                => Some("V")
        case DefDef(_,_,_,_,_,_)            => Some("m")
        case TypeDef(_,_,_,_)               => Some("T")
        case _ => None
      }

      def scope(t: Tree) = t match {
        case PackageDef(_,_)                => Some("package")
        case ModuleDef(m,_,_)  if m.isCase  => Some("case_object")
        case ModuleDef(_,_,_)               => Some("object")
        case ClassDef(m,_,_,_) if m.isCase  => Some("case_class")
        case ClassDef(m,_,_,_) if m.isTrait => Some("trait")
        case ClassDef(m,_,_,_)              => Some("class")
        case DefDef(_,_,_,_,_,_)            => Some("method")
        case _ => None

      }

      override def traverse(t: Tree): Unit = {
        val line = t.pos.line
        val col  = t.pos.column
        val text = t.pos.lineContent
        var fields: immutable.Map[String, String] = immutable.Map.empty
        access(t).foreach(fields += "access" -> _)
        implementation(t).foreach(fields += "implementation" -> _)
        kind(t).foreach(fields += "kind" -> _)
        val name = t match {
          case (dtree: DefTree) => Some(dtree.name)
          case _ => None
        }

        if (name.isDefined && fields.contains("kind")) {
          if (!path.isEmpty) {
            fields += path.head._1 -> path.map(_._2).reverse.mkString(".")
          }
          // push scope to path
          scope(t).foreach(s => path.push(s -> name.get.decode))

          addTag(TagPosition(line, col, text), name.get, fields)
        }

        super.traverse(t)

        if (name.isDefined && fields.contains("kind")) {
          // pop scope from path
          scope(t).foreach(_ => path.pop())
        }
      }
    }
    val traverser = new GenTagTraverser
    traverser.traverse(tree)
    traverser.tags
  }
}
