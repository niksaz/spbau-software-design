package shell.command

import java.io.BufferedOutputStream
import java.nio.file.{Files, Paths}

import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.Path

/** Command for writing file content to stdout. */
class CatCommandRunner extends CommandRunner("cat") {
  override def run(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    val outputStream = new BufferedOutputStream(ioEnvironment.printStream)
    def writeBytesToOutput(bytes: Array[Byte]): Unit = {
      outputStream.write(bytes)
      outputStream.flush()
    }
    if (args.isEmpty) {
      val bytes =
        Stream.continually(ioEnvironment.inputStream.read).takeWhile(-1 != _).map(_.toByte).toArray
      writeBytesToOutput(bytes)
    } else {
      args.foreach { arg =>
        val filePath = environment.currentDir.resolve(Path(arg))
        val bytes = Files.readAllBytes(Paths.get(filePath.toString()))
        writeBytesToOutput(bytes)
      }
    }
  }
}