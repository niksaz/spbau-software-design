package model

import scala.reflect.io.{Directory, Path}

/** Stores the environment necessary of the commands. */
case class Environment private (currentDir: Directory, variables: Map[String, String])
    extends VariableSupplier {
  /** Returns the value for a given variable. */
  override def retrieveVariable(variableName: String): String =
    variables.getOrElse(variableName, "")

  def updateVariable(variableName: String, value: String): Environment = {
    val updatedVariables = variables + ((variableName, value))
    Environment(currentDir, updatedVariables)
  }
}

object Environment {
  def apply(currentPath: Path): Environment =
    new Environment(Directory(currentPath), Map[String, String]())
}