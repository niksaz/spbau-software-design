package command
import java.io.PrintStream
import java.nio.file.{Files, Paths}

import model.{Environment, IOEnvironment}

import scala.reflect.io.Path

/** Command for counting lines, words and bytes in a file. */
class WcCommandRunner extends CommandRunner("wc") {
  override def run(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    if (args.isEmpty) {
      val bytes = Stream.continually(ioEnvironment.inputStream.read).takeWhile(-1 != _).map(_.toByte).toArray
      countAndPrintStatisticsFor(bytes, ioEnvironment.printStream)
    } else {
      args.foreach { arg =>
        val filePath = environment.currentDir.resolve(Path(arg))
        val bytes = Files.readAllBytes(Paths.get(filePath.toString()))
        countAndPrintStatisticsFor(bytes, ioEnvironment.printStream)
      }
    }
  }

  private def countAndPrintStatisticsFor(bytes: Array[Byte], printStream: PrintStream): Unit = {
    val separatorBytes = System.lineSeparator().getBytes
    var i = 0
    var wordCount = 0
    var lineCount = 1
    while (i < bytes.length) {
      if (areNextBytesFollowing(bytes, i, separatorBytes)) {
        lineCount += 1
        i += separatorBytes.length
      } else if (bytes(i) == ' ') {
        i += 1
      } else {
        wordCount += 1
        while (i < bytes.length && !areNextBytesFollowing(bytes, i, separatorBytes)
          && bytes(i) != ' ') {
          i += 1
        }
      }
    }
    val byteCount = bytes.length
    printStream.println(s"$lineCount $wordCount $byteCount")
  }

  private def areNextBytesFollowing(bytes: Array[Byte], pos: Int, pattern: Array[Byte]): Boolean = {
    if (pos + pattern.length > bytes.length) {
      return false
    }
    var k = 0
    while (k < pattern.length && pattern(k) == bytes(pos + k)) {
      k += 1
    }
    k == pattern.length
  }
}