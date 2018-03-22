package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import org.scalatest.FunSuite
import shell.Converter
import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.Path

class GrepCommandRunnerTest extends FunSuite {
  private val grepCommandRunner = new GrepCommandRunner
  private val environment = Environment(Path("")).registerCommandRunner(grepCommandRunner)

  test("testInputFromStdIn") {
    val inputStream = new ByteArrayInputStream(
      Converter.getLineBytes(endWithSeparator = true, "hello", "my", "name"))
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream, true)
    grepCommandRunner.run(List("m"), environment, IOEnvironment(inputStream, printStream))
    val expectedBytes = Converter.getLineBytes(endWithSeparator = true, "my", "name")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }

  test("testInputFromFile") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))
    val filename = "grepCommand.txt"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)
    val printWriter = tempFile.toFile.printWriter()
    printWriter.println("It is fuuuuuuuu")
    printWriter.println("to create temporary directories if you are not you")
    printWriter.close()

    val customEnvironment = Environment(tempDir).registerCommandRunner(grepCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream, true)
    grepCommandRunner.run(
      List("uu+", filename), customEnvironment, IOEnvironment(inputStream, printStream))
    val expectedBytes = Converter.getLineBytes(endWithSeparator = true, "It is fuuuuuuuu")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }

  test("testIgnoreCase") {
    val inputStream = new ByteArrayInputStream(
      Converter.getLineBytes(endWithSeparator = true, "hello", "My", "NaMY"))
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream, true)
    grepCommandRunner.run(List("-i", "my"), environment, IOEnvironment(inputStream, printStream))
    val expectedBytes = Converter.getLineBytes(endWithSeparator = true, "My", "NaMY")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }

  test("testWordsOnly") {
    val inputStream = new ByteArrayInputStream(
      Converter.getLineBytes(endWithSeparator = true, "whats up"))
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream, true)
    grepCommandRunner.run(List("-w", "s.u"), environment, IOEnvironment(inputStream, printStream))
    assert(outputStream.toByteArray.isEmpty)
  }

  test("testAfterContext") {
    val inputStream = new ByteArrayInputStream(
      Converter.getLineBytes(endWithSeparator = true, "to", "put", "your", "armor", "on"))
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream, true)
    grepCommandRunner.run(
      List("-A", "1", "u"), environment, IOEnvironment(inputStream, printStream))
    val expectedBytes = Converter.getLineBytes(endWithSeparator = true, "put", "your", "armor")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }
}
