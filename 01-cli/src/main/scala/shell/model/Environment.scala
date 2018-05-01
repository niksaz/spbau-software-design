package shell.model

import shell.command.CommandRunner

import scala.reflect.io.{Directory, Path}

/** Stores the environment necessary of the commands. */
case class Environment private (
    private var _currentDir: Directory,
    variables: Map[String, String],
    commandToRunners: Map[String, CommandRunner]) extends VariableSupplier {
  def currentDir: Directory = _currentDir

  /** Returns the value for a given variable. */
  override def retrieveVariable(variableName: String): String =
    variables.getOrElse(variableName, "")

  def updateVariable(variableName: String, value: String): Environment = {
    val updatedVariables = variables + ((variableName, value))
    Environment(currentDir, updatedVariables, commandToRunners)
  }

  def updateCurrentDirectory(newDirectory: Directory): Unit =
    _currentDir = newDirectory

  def registerCommandRunner(commandRunner: CommandRunner): Environment = {
    Environment(currentDir, variables, commandToRunners + (commandRunner.name -> commandRunner))
  }
}

object Environment {
  def apply(currentPath: Path): Environment =
    new Environment(Directory(currentPath), Map(), Map())
}