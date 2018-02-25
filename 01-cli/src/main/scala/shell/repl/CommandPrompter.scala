package shell.repl

/** An entity which asks the user to enter the next shell.command. */
trait CommandPrompter {
  /** Prompts the user to enter the next shell.command. */
  def promptUser(): Unit
}
