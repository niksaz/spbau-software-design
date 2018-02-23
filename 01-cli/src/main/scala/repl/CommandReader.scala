package repl

/** A trait that stands for an entity suitable for reading commands from a user. */
trait CommandReader {
  /** Reads the next command from a user. */
  def receiveNextCommandLine(): String
}