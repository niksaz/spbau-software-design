package command

import model.{Environment, IOEnvironment}

/** Command for printing current directory. */
class PwdCommandRunner extends CommandRunner("pwd") {
  override def run(
      args: List[String], environment: Environment, iOEnvironment: IOEnvironment): Unit = {
    iOEnvironment.printStream.println(environment.currentDir)
  }
}