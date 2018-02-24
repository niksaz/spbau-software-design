package command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import model.{Environment, IOEnvironment}
import org.scalatest.FunSuite

import scala.reflect.io.Path

class WcCommandRunnerTest extends FunSuite {
  test("wcCommandFromFile") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))
    val filename = "wcCommandFromFile.txt"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)
    val printWriter = tempFile.toFile.printWriter()
    printWriter.println("It is fun")
    printWriter.println("to rewrite Unix utilities")
    printWriter.print("from start!")
    printWriter.close()

    val wcCommandRunner = new WcCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(wcCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    wcCommandRunner.run(List(filename), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expected = "3 9 47\n".getBytes
    assert(outputStream.toByteArray sameElements expected)
  }

  test("wcCommandFromStdin") {
    val wcCommandRunner = new WcCommandRunner
    val environment = Environment(Path("")).registerCommandRunner(wcCommandRunner)
    val inputStream = new ByteArrayInputStream("Hello,\n world!\n".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    wcCommandRunner.run(List(), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expected = "2 2 15\n".getBytes
    assert(outputStream.toByteArray sameElements expected)
  }
}