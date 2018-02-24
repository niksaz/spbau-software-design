package command

import model.{Environment, IOEnvironment}

/** Base class for an entity that can run a specific command. */
abstract class CommandRunner(val name: String) {
  /** Runs the command with the given args, [[Environment]] and in [[IOEnvironment]]. */
  def run(args: List[String], environment: Environment, ioEnvironment: IOEnvironment)
}