package shell.repl

import shell.command.{CatCommandRunner, EchoCommandRunner, PwdCommandRunner, WcCommandRunner}
import shell.lexical.{Lexer, LexicalAnalyzer, LexicalParsingException}
import shell.model.{Environment, IOEnvironment}
import shell.process.{CommandProcessor, SequentialCommandProcessor}

import scala.reflect.io.Path

/** Read-eval-print loop for interpreting commands. */
class Repl(
    val commandReader: CommandReader,
    val commandPrompter: CommandPrompter,
    val errorDisplayer: ErrorDisplayer,
    val ioEnvironment: IOEnvironment,
    val currentPath: Path) {
  /** Starts the shell.command processing. */
  def run(): Unit = {
    val lexer: LexicalAnalyzer = new Lexer()
    var environment = Environment(currentPath)
    environment = registerDefaultCommandRunners(environment)
    val commandProcessor: CommandProcessor = new SequentialCommandProcessor
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
    List(new PwdCommandRunner, new EchoCommandRunner, new CatCommandRunner, new WcCommandRunner)
      .foreach { commandRunner =>
      newEnvironment = newEnvironment.registerCommandRunner(commandRunner)
    }
    newEnvironment
  }
}