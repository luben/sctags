package sctags

import java.io.PrintStream

object CTags {

  private class CaseInsensitiveOrder(val self: String) extends Ordered[String] {
    def compare(that: String) = self.compareToIgnoreCase(that)
  }

  private val header = Array(
    "!_TAG_FILE_FORMAT\t2\t//",
    "!_TAG_FILE_SORTED\t2\t/0=unsorted, 1=sorted, 2=sorted,casefold/"
  )

  private def formatTags(t: (String, Seq[Tag])) = {
    val file = t._1
    val tags = t._2
    tags.map(tag => {
      val pos = "/^" + tag.pos.content.replace("\\","\\\\") + "$/"
      tag.name + "\t" + file + "\t" + pos + tag.fieldsString
    })
  }

  def generate(tags: Seq[(String, Seq[Tag])], output: PrintStream) {
    val tagStrings = (tags.flatMap(formatTags _) ++ header).sorted.toArray
    tagStrings foreach {l => output.println(l)}
  }
}
