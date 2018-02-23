package repl

import java.io.{InputStream, OutputStream}

import lexical.{Lexer, LexicalParsingException}
import model.Environment
import process.SequentialCommandProcessor

import scala.reflect.io.Path

/** Read-eval-print loop for interpreting commands. */
class Repl(
    val commandReader: CommandReader,
    val commandPrompter: CommandPrompter,
    val errorDisplayer: ErrorDisplayer,
    val initialInputStream: InputStream,
    val finalOutputStream: OutputStream,
    val currentPath: Path) {
  /** Starts the command processing. */
  def run(): Unit = {
    val lexer = new Lexer()
    var environment = Environment(currentPath)
    val commandProcessor = new SequentialCommandProcessor
    while (true) {
      try {
        commandPrompter.promptUser()
        val nextCommandLine = commandReader.receiveNextCommandLine()
        val commands = lexer.analyze(nextCommandLine)
        environment =
          commandProcessor.processCommandSequence(
            commands, environment, initialInputStream, finalOutputStream)
      } catch {
        case e: LexicalParsingException =>
          errorDisplayer.showErrorMessage(s"Lexical error occurred: ${e.getMessage}")
        case e: Exception =>
          errorDisplayer.showErrorMessage(s"Error occurred: ${e.getMessage}")
      }
    }
  }
}
