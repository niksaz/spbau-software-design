package repl

/** An entity which asks the user to enter the next command. */
trait CommandPrompter {
  /** Prompts the user to enter the next command. */
  def promptUser(): Unit
}
