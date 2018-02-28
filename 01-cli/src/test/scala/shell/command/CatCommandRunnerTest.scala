package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import org.scalatest.FunSuite
import shell.Converter
import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.Path

class CatCommandRunnerTest extends FunSuite {
  test("catCommand") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))
    val filename = "catCommand.txt"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)
    val printWriter = tempFile.toFile.printWriter()
    printWriter.println("It is fun")
    printWriter.println("to create temporary directories")
    printWriter.print("in Scala!")
    printWriter.close()

    val catCommandRunner = new CatCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(catCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    catCommandRunner.run(List(filename), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expectedBytes = Converter.getLineBytes(
      endWithSeparator = false, "It is fun", "to create temporary directories", "in Scala!")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }

  test("catCommandDefaultInputStream") {
    val catCommandRunner = new CatCommandRunner
    val environment = Environment(Path("")).registerCommandRunner(catCommandRunner)
    val inputStream = new ByteArrayInputStream("weirdo".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    catCommandRunner.run(List(), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expectedBytes = "weirdo".getBytes
    assert(outputStream.toByteArray sameElements expectedBytes)
  }
}