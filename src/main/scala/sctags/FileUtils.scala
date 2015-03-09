package sctags

import java.io.{File, FileFilter}
import java.util.regex.Pattern

import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

object FileUtils {

  implicit def fun2fileFilter(fun: File => Boolean) =
    new FileFilter { def accept(f: File) = fun(f) }

  implicit def fileFilter2richFilter(filter: FileFilter): RichFilter =
    new RichFilter(filter)

  final class RichFilter(val self: FileFilter) extends Proxy {
    def unary_! = new FileFilter { def accept(f: File) = !self.accept(f) }
    def join(other: FileFilter, op: (Boolean,Boolean)=>Boolean) =
      new FileFilter { def accept(f: File) = op(self.accept(f), other.accept(f)) }
    def &&(other: FileFilter) = join(other, _ && _);
    def ||(other: FileFilter) = join(other, _ || _);
  }

  object DirectoryFilter extends FileFilter {
    def accept(f: File) = f.isDirectory;
  }

  class NameMatchFilter(val re: Pattern) extends FileFilter {
    def this(re: String) = this(Pattern.compile(re));
    def accept(f: File) = re.matcher(f.getName).matches;
  }

  object AcceptAllFilter extends FileFilter {
    def accept(f: File) = true;
  }

  def listFilesRecursive(base: File, filter: FileFilter): Seq[File] = {
    val files = new ArrayBuffer[File];
    def processdir(dir: File) {
      val directories = dir.listFiles(DirectoryFilter).foreach(d => processdir(d))
      val matchedFiles = dir.listFiles(filter)
      files ++= matchedFiles
    }
    processdir(base)
    files
  }
}
