package shell.repl

/** A trait that stands for an entity suitable for reading commands from a user. */
trait CommandReader {
  /** Reads the next shell.command from a user. */
  def receiveNextCommandLine(): String
}