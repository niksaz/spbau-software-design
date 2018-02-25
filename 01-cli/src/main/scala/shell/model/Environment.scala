package shell.model

import shell.command.CommandRunner

import scala.reflect.io.{Directory, Path}

/** Stores the environment necessary of the commands. */
case class Environment private (
    currentDir: Directory,
    variables: Map[String, String],
    commandRunners: List[CommandRunner]) extends VariableSupplier {
  /** Returns the value for a given variable. */
  override def retrieveVariable(variableName: String): String =
    variables.getOrElse(variableName, "")

  def updateVariable(variableName: String, value: String): Environment = {
    val updatedVariables = variables + ((variableName, value))
    Environment(currentDir, updatedVariables, commandRunners)
  }

  def registerCommandRunner(commandRunner: CommandRunner): Environment = {
    Environment(currentDir, variables, commandRunner :: commandRunners)
  }
}

object Environment {
  def apply(currentPath: Path): Environment =
    new Environment(Directory(currentPath), Map(), List())
}