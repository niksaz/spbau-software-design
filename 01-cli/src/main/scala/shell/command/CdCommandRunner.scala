package shell.command

import java.io.FileNotFoundException

import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.{Directory, Path}

/** Command for changing working directory.
  * In case no arguments are provided new directory is user home.
  */
class CdCommandRunner extends CommandRunner("cd") {
  override def run(
                    args: List[String],
                    environment: Environment,
                    ioEnvironment: IOEnvironment
                  ): Unit = {
    val newPath: Path = args.size match {
      case 0 => Path.apply(".")
      case 1 =>
        val arg = Directory(args.head)
        val result = if (arg.isAbsolute) {
          arg
        } else {
          environment.currentDir / arg
        }
        if (!result.exists) {
          throw new FileNotFoundException("Location not found")
        } else if (!result.isDirectory) {
          throw new FileNotFoundException("Location is not a directory")
        } else {
          result
        }
      case _ => throw new IllegalArgumentException("Too many arguments")
    }
    environment.updateCurrentDirectory(
      newPath.toCanonical.toDirectory
    )
  }
}