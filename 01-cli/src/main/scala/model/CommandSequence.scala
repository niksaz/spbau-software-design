package model

/** Represents an entered sequence of [[Command]]s. */
case class CommandSequence(commands: List[Command]) {
  /** Returns the number of commands in the command sequence. */
  def length: Int = commands.length
}