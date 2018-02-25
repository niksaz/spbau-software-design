package shell.model

import shell.command.CommandRunner

import scala.reflect.io.{Directory, Path}

/** Stores the environment necessary of the commands. */
case class Environment private (
    currentDir: Directory,
    variables: Map[String, String],
    commandToRunners: Map[String, CommandRunner]) extends VariableSupplier {
  /** Returns the value for a given variable. */
  override def retrieveVariable(variableName: String): String =
    variables.getOrElse(variableName, "")

  def updateVariable(variableName: String, value: String): Environment = {
    val updatedVariables = variables + ((variableName, value))
    Environment(currentDir, updatedVariables, commandToRunners)
  }

  def registerCommandRunner(commandRunner: CommandRunner): Environment = {
    Environment(currentDir, variables, commandToRunners + (commandRunner.name -> commandRunner))
  }
}

object Environment {
  def apply(currentPath: Path): Environment =
    new Environment(Directory(currentPath), Map(), Map())
}