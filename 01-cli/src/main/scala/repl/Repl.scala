package repl

import java.io.{InputStream, OutputStream, PrintStream}

import lexical.{Lexer, LexicalParsingException}

/** Read-eval-print loop for interpreting commands. */
class Repl(
            val commandReader: CommandReader,
            val commandPrompter: CommandPrompter,
            val errorDisplayer: ErrorDisplayer,
            val initialInputStream: InputStream,
            val finalOutputStream: OutputStream) {
  /** Starts the command processing. */
  def run(): Unit = {
    val DEBUG_PRINT_STREAM = new PrintStream(finalOutputStream)
    val lexer = new Lexer()
    while (true) {
      commandPrompter.promptUser()
      val nextCommandLine = commandReader.receiveNextCommandLine()
      try {
        val commands = lexer.analyze(nextCommandLine)
        DEBUG_PRINT_STREAM.println(commands)
      } catch {
        case e: LexicalParsingException =>
          errorDisplayer.showErrorMessage(s"Lexical error occurred: ${e.getMessage}")
      }
    }
  }
}
