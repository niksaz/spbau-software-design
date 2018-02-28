package shell

import java.util.Scanner

import shell.repl.{CommandPrompter, CommandReader, ErrorDisplayer}

/** Interacts with user through stdin and stdout. */
class CliIO extends CommandPrompter with CommandReader with ErrorDisplayer {
  private val scanner = new Scanner(System.in)
  private val printStream = System.out
  private val errorPrintStream = System.err

  /** Prompts the user to enter the next command. */
  override def promptUser(): Unit = {
    printStream.print("> ")
  }

  /** Reads the next shell.command from a user. */
  override def receiveNextCommandLine(): String = scanner.nextLine()

  /** Show the given error to the user. */
  override def showErrorMessage(message: String): Unit = errorPrintStream.println(message)
}
