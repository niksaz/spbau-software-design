package process
import java.io.{InputStream, OutputStream, PrintStream}

import model.{CommandSequence, Environment}

/** [[CommandProcessor]] which processes command sequentially. */
class SequentialCommandProcessor extends CommandProcessor {
  override def processCommandSequence(
      commandSequence: CommandSequence,
      environment: Environment,
      initialInputStream: InputStream,
      finalOutputStream: OutputStream): Environment = {
    val DEBUG_PRINT_STREAM = new PrintStream(finalOutputStream)
    DEBUG_PRINT_STREAM.println(commandSequence)
    environment
  }
}