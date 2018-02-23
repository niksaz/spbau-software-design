package process

import java.io.{InputStream, OutputStream}

import model.{CommandSequence, Environment}

/** An entity that is able to process [[CommandSequence]]s. */
trait CommandProcessor {
  /**
    * Process the [[CommandSequence]] in the [[Environment]] with the given input and output
    * streams.
    */
  def processCommandSequence(
      commandSequence: CommandSequence,
      environment: Environment,
      initialInputStream: InputStream,
      finalOutputStream: OutputStream): Environment
}