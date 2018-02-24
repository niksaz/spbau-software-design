package process
import java.io.PrintStream

import model.{CommandSequence, Environment, IOEnvironment}

import scala.collection.mutable

/** [[CommandProcessor]] which processes command sequentially. */
class SequentialCommandProcessor extends CommandProcessor {
  override def processCommandSequence(
      commandSequence: CommandSequence,
      environment: Environment,
      ioEnvironment: IOEnvironment): ProcessingResult = {
    val DEBUG_PRINT_STREAM = new PrintStream(ioEnvironment.outputStream)
    for (command <- commandSequence.commands) {
      val args = mutable.ListBuffer[String]()
      for (word <- command.words) {
        val argBuilder = new mutable.StringBuilder
        for (wordPart <- word.parts) {
          argBuilder.append(wordPart.resolve(environment))
        }
        args.append(argBuilder.toString())
      }
      DEBUG_PRINT_STREAM.println(args)
      if (args.nonEmpty && args.head == "exit") {
        return ProcessingResult(environment, shouldExit = true)
      }
    }
    ProcessingResult(environment, shouldExit = false)
  }
}