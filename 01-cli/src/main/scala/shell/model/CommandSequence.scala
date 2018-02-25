package shell.model

/** Represents an entered sequence of [[Command]]s. */
case class CommandSequence(commands: List[Command]) {
  /** Returns the number of commands in the shell.command sequence. */
  def length: Int = commands.length
}