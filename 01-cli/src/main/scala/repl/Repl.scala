package repl

import command.{EchoCommandRunner, PwdCommandRunner}
import lexical.{Lexer, LexicalParsingException}
import model.{Environment, IOEnvironment}
import process.SequentialCommandProcessor

import scala.reflect.io.Path

/** Read-eval-print loop for interpreting commands. */
class Repl(
    val commandReader: CommandReader,
    val commandPrompter: CommandPrompter,
    val errorDisplayer: ErrorDisplayer,
    val ioEnvironment: IOEnvironment,
    val currentPath: Path) {
  /** Starts the command processing. */
  def run(): Unit = {
    val lexer = new Lexer()
    var environment = Environment(currentPath)
    environment = registerDefaultCommandRunners(environment)
    val commandProcessor = new SequentialCommandProcessor
    while (true) {
      try {
        commandPrompter.promptUser()
        val nextCommandLine = commandReader.receiveNextCommandLine()
        val commands = lexer.analyze(nextCommandLine)
        val result = commandProcessor.processCommandSequence(commands, environment, ioEnvironment)
        if (result.shouldExit) {
          return
        }
        environment = result.environment
      } catch {
        case e: LexicalParsingException =>
          errorDisplayer.showErrorMessage(s"Lexical error occurred: ${e.getMessage}")
        case e: Exception =>
          errorDisplayer.showErrorMessage(s"Error occurred: ${e.getMessage}")
      }
    }
  }

  private def registerDefaultCommandRunners(environment: Environment): Environment = {
    var newEnvironment = environment
    newEnvironment = newEnvironment.registerCommandRunner(new PwdCommandRunner)
    newEnvironment = newEnvironment.registerCommandRunner(new EchoCommandRunner)
    newEnvironment
  }
}