package sctags

import java.io.PrintStream

object CTags {

  private val header = List(
    "!_TAG_FILE_FORMAT\t2\t//",
    "!_TAG_FILE_SORTED\t1\t/0=unsorted, 1=sorted, 2=sorted,casefold/"
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
    val tagStrings = (header ++ tags.flatMap(formatTags _).sorted).toArray
    tagStrings foreach {l => output.println(l)}
  }
}
