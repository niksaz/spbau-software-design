package command
import java.io.BufferedOutputStream
import java.nio.file.{Files, Paths}

import model.{Environment, IOEnvironment}

import scala.reflect.io.Path

/** Command for writing file content to stdout. */
class CatCommandRunner extends CommandRunner("cat") {
  override def run(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    val outputStream = new BufferedOutputStream(ioEnvironment.printStream)
      args.foreach { arg =>
        val filePath = environment.currentDir.resolve(Path(arg))
        val bytes = Files.readAllBytes(Paths.get(filePath.toString()))
        outputStream.write(bytes)
        outputStream.flush()
      }
    }
}