package shell.command

import java.io.PrintStream

import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.{Directory, Path}

/** Command for writing directory contents to stdout. */
class LsCommandRunner extends CommandRunner("ls") {
  override def run(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    val printStream = new PrintStream(ioEnvironment.printStream)
    val userHome = System.getProperty("user.dir")
    (if (args.isEmpty) {
      List(Path.apply("."))
    } else {
      args.map { arg => Path.apply(arg) }
    }).map { arg =>
      Directory.apply(
        if (arg.isAbsolute) {
          arg
        } else {
          (environment.currentDir / arg).toCanonical.toDirectory
        }
      )
    }.filter { arg =>
      if (!arg.exists) {
        printStream.println(s"could not find $arg")
        false
      } else if (arg.isFile) {
        printStream.println(arg)
        false
      } else {
        true
      }
    }.foreach { arg =>
      printStream.println(s"$arg:")
      arg.list.foreach { subpath => printStream.println(s"\t${arg.relativize(subpath)}") }
    }
  }
}