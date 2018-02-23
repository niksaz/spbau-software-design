import java.util.Scanner

import repl.{CommandPrompter, CommandReader, ErrorDisplayer, Repl}

/** Command line interface for command interpreting. */
object Cli extends CommandPrompter with CommandReader with ErrorDisplayer {
  private val inputStream = System.in
  private val outputStream = System.out
  private val scanner = new Scanner(inputStream)
  private val printStream = System.out
  private val errorPrintStream = System.err

  def main(args: Array[String]): Unit = {
    val repl = new Repl(this, this, this, inputStream, outputStream)
    repl.run()
  }

  /** Prompts the user to enter the next command. */
  override def promptUser(): Unit = {
    printStream.print("> ")
  }

  /** Reads the next command from a user. */
  override def receiveNextCommandLine(): String = scanner.nextLine()

  /** Show the given error to the user. */
  override def showErrorMessage(message: String): Unit = errorPrintStream.println(message)
}
