package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.nio.file.Files

import org.scalatest.FunSuite
import shell.Converter
import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.{Directory, Path}

class LsCommandRunnerTest extends FunSuite {
  test("lsCommand") {
    val tempDir = new Directory(Files.createTempDirectory("myTempDir").toFile)
    val filename = "lsCommand.txt"
    val nonExistantFilename = "lsCommand.missing"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)
    val missingTempFile = tempDir / nonExistantFilename

    val lsCommandRunner = new LsCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(lsCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    lsCommandRunner.run(
      List(tempDir.toString, ".", filename, nonExistantFilename),
      environment,
      IOEnvironment(inputStream, printStream)
    )
    printStream.flush()
    val expectedBytes = Converter.getLineBytes(
      endWithSeparator = true,
      s"${tempFile.toAbsolute}",
      s"could not find ${missingTempFile.toAbsolute}",
      s"${tempDir.toAbsolute}:",
      s"${"\t"}${tempFile.toAbsolute}",
      s"${(tempDir / ".").toAbsolute}:",
      s"${"\t"}${(tempDir / "." / filename).toAbsolute}"
    )
    assert(outputStream.toByteArray sameElements expectedBytes)
  }

  test("lsCommandNoArgs") {
    val tempDir = new Directory(Files.createTempDirectory("myTempDir").toFile)
    val filename = "lsCommand.txt"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)

    val lsCommandRunner = new LsCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(lsCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    lsCommandRunner.run(
      List(),
      environment,
      IOEnvironment(inputStream, printStream)
    )
    printStream.flush()
    val expectedBytes = Converter.getLineBytes(
      endWithSeparator = true,
      s"${(tempDir / ".").toAbsolute}:",
      s"${"\t"}${(tempDir / "." / filename).toAbsolute}"
    )
    assert(outputStream.toByteArray sameElements expectedBytes)
  }
}