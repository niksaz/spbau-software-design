package shell.command

import shell.model.{Environment, IOEnvironment}

/** Command for printing everything from input to output. */
class EchoCommandRunner extends CommandRunner("echo") {
  override def run(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    args.foreach { arg =>
      ioEnvironment.printStream.println(arg)
    }
  }
}