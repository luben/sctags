package sctags

import scala.collection.mutable.StringBuilder

case class Tag(val name: String, pos: TagPosition, fields: Product2[String, String]*) {
  private def fieldString(field: Product2[String, String]) =
    if (field._1 == "kind") escapeValue(field._2)
      else field._1 + ":" + escapeValue(field._2)

  private def escapeValue(value: String) =
    (value.foldLeft(new StringBuilder) {(b, c) =>
      c match {
        case '\t' => b.append("\\t")
        case '\r' => b.append("\\r")
        case '\n' => b.append("\\n")
        case '\\' => b.append("\\\\")
        case x    => b.append(x)
      }
    }).toString

  def fieldsString: String =
    if (fields.isEmpty) {
      ""
    } else {
      (fields ++ Seq("line"->pos.line.toString)).
        map(fieldString).mkString(";\"\t", "\t", "")
    }
  override def toString =
    name + "\t" + "\t" + pos.content + fieldsString
}
