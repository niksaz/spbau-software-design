package command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import model.{Environment, IOEnvironment}
import org.scalatest.FunSuite

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
    val expected = "It is fun\nto create temporary directories\nin Scala!".getBytes
    assert(outputStream.toByteArray sameElements expected)
  }
}