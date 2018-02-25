package shell

import java.io.ByteArrayInputStream
import java.util.Scanner

import shell.model.IOEnvironment
import shell.repl.{CommandPrompter, CommandReader, ErrorDisplayer, Repl}

import scala.reflect.io.Path

/** Command line interface for shell.command interpreting. */
object Cli extends CommandPrompter with CommandReader with ErrorDisplayer {
  private val inputStream = new ByteArrayInputStream("".getBytes)
  private val scanner = new Scanner(System.in)
  private val printStream = System.out
  private val errorPrintStream = System.err

  def main(args: Array[String]): Unit = {
    val currentPath = Path("").toAbsolute
    val repl = new Repl(this, this, this, IOEnvironment(inputStream, printStream), currentPath)
    repl.run()
  }

  /** Prompts the user to enter the next shell.command. */
  override def promptUser(): Unit = {
    printStream.print("> ")
  }

  /** Reads the next shell.command from a user. */
  override def receiveNextCommandLine(): String = scanner.nextLine()

  /** Show the given error to the user. */
  override def showErrorMessage(message: String): Unit = errorPrintStream.println(message)
}
