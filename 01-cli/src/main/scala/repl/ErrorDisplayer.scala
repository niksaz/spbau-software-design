package repl

/** An entity which is capable of dealing with errors. */
trait ErrorDisplayer {
  /** Show the given error to the user. */
  def showErrorMessage(message: String)
}
