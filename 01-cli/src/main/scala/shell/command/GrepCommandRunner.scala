package shell.command

import java.util.Scanner

import org.rogach.scallop.{ScallopConf, ScallopOption}
import shell.command.GrepCommandRunner.GrepArgsConfiguration
import shell.model.{Environment, IOEnvironment}

/** [[CommandRunner]] for a grep-like utility. */
class GrepCommandRunner extends CommandRunner("grep") {
  override def run(
    args: List[String], environment: Environment, ioEnvironment: IOEnvironment
  ): Unit = {
    try {
      val config = new GrepArgsConfiguration(args)
      val pattern = (if (config.i()) config.pattern().toLowerCase else config.pattern()).r
      val inputStreams =
        if (config.filenames.isEmpty) List(ioEnvironment.inputStream)
        else config.filenames().map(environment.currentDir.resolve(_).toFile.inputStream())
      inputStreams.foreach { inputStream =>
        val scanner = new Scanner(inputStream)
        var shouldPrintLines = 0
        while (scanner.hasNext) {
          val line = scanner.nextLine()
          val lineForSearch = if (config.i()) line.toLowerCase else line
          val isLineMatches = if (config.w()) {
            lineForSearch.split("\\s+").exists(word => pattern.findFirstIn(word).isDefined)
          } else {
            pattern.findFirstIn(lineForSearch).isDefined
          }
          if (isLineMatches) {
            shouldPrintLines = 1 + config.a()
          }
          if (shouldPrintLines > 0) {
            ioEnvironment.printStream.println(line)
            shouldPrintLines -= 1
          }
        }
        inputStreams.foreach(_.close())
      }
    } catch {
      case t: Throwable => System.err.println(s"grep: ${t.getMessage}")
    }
  }
}

object GrepCommandRunner {
  /** [[ScallopConf]] for the parsing of [[GrepCommandRunner]] args. */
  private class GrepArgsConfiguration(args: Seq[String]) extends ScallopConf(args) {
    val i: ScallopOption[Boolean] = opt[Boolean]()
    val w: ScallopOption[Boolean] = opt[Boolean]()
    val a: ScallopOption[Int] = opt[Int](short = 'A', default = Some(0), validate = 0<=_)
    val pattern: ScallopOption[String] = trailArg[String](required = true)
    val filenames: ScallopOption[List[String]] = trailArg[List[String]](required = false)
    verify()
    override def onError(e: Throwable): Unit = throw e
  }
}
