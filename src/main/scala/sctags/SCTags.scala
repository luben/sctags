package sctags

import scala.tools.nsc.{Settings, Global}
import scala.tools.nsc.reporters.StoreReporter

import scala.collection.mutable.ListBuffer


import java.io.File
import java.io.PrintStream

object SCTags extends Parsing with TagGeneration
{

  import FileUtils._;

  var outputFile: String = "tags";
  var recurse = false;
  var etags = false

  def parseOpt(args:List[String]): List[String] =
    args match {
      case ("-f" |"-o")         :: file :: rest => outputFile = file; parseOpt(rest)
      case ("-r" |"--recurse" ) :: rest => recurse = true;            parseOpt(rest)
      case ("-e" |"--etags"   ) :: rest => etags = true;              parseOpt(rest)
      case ("-v" |"--version")  :: rest => println("Exuberant Ctags 5.9~fake"); Nil
      case files  => files
    }

  def error(str: String) = System.err.println("Error: " + str);
  val settings = new Settings(error);
  val reporter = new StoreReporter;
  val compiler = new Global(settings, reporter);


  def run(fnames: Seq[String]) {
    val files = new ListBuffer[File]
    fnames foreach { fname =>
      val file = new File(fname)
      if (file.isDirectory) {
        if (recurse)
          files ++= listFilesRecursive(file, {(f: File) => f.getName.endsWith(".scala")})
        else
          System.err.println("Skipping directory " + fname);
      } else {
        if (file.getName.endsWith(".scala"))
          files += file
        else
          System.err.println("Skipping file " + fname);
      }
    }

    val tags = files.map(f => (f.getPath, generateTags(parse(f))))
    val output = outputFile match {
      case "-" => Console.out
      case "tags" if etags =>  new PrintStream("TAGS")
      case x => new PrintStream(x)
    }

    if (etags) {
      ETags.generate(tags, output)
    } else {
      CTags.generate(tags, output)
    }
  }

  def main(args: Array[String]): Unit = {
    val fnames = parseOpt(args.toList)
    run(fnames)
  }
}
