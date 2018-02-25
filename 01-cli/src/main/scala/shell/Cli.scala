package shell

import java.io.ByteArrayInputStream

import shell.model.IOEnvironment
import shell.repl.Repl

import scala.reflect.io.Path

/** Object for starting up command line interface. */
object Cli {
  private val inputStream = new ByteArrayInputStream("".getBytes)
  private val printStream = System.out

  def main(args: Array[String]): Unit = {
    val currentPath = Path("").toAbsolute
    val cliIO = new CliIO
    val repl = new Repl(cliIO, cliIO, cliIO, IOEnvironment(inputStream, printStream), currentPath)
    repl.run()
  }
}
