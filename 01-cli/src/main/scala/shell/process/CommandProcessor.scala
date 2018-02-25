package shell.process

import shell.model.{CommandSequence, Environment, IOEnvironment}

/** An entity that is able to shell.process [[CommandSequence]]s. */
trait CommandProcessor {
  /**
    * Process the [[CommandSequence]] in the [[Environment]] with the given [[IOEnvironment]].
    */
  def processCommandSequence(
      commandSequence: CommandSequence,
      environment: Environment,
      iOEnvironment: IOEnvironment): ProcessingResult

  case class ProcessingResult(environment: Environment, shouldExit: Boolean)
}