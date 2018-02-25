package shell.model

/** A trait which represents an ability to retrieve variables. */
trait VariableSupplier {
  /** Returns the value for a given variable. */
  def retrieveVariable(variableName: String): String
}
