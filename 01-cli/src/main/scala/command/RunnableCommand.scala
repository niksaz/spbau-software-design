package command

/** Base class for command which can be run. */
abstract class RunnableCommand(val name: String) {
  /** Runs the command with the given args. */
  def run(args: List[String])
}