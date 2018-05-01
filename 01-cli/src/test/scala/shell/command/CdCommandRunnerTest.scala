package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileNotFoundException, PrintStream}

import org.scalatest.FunSuite
import shell.model.{Environment, IOEnvironment}

import scala.reflect.io.Path

class CdCommandRunnerTest extends FunSuite {
  test("cdCommandSameDir") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    cdCommandRunner.run(List("."), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
    assert(environment.currentDir isSame tempDir)
  }

  test("cdCommandParentDir") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    cdCommandRunner.run(List(".."), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
    assert(environment.currentDir isSame tempDir.parent)
  }

  test("cdCommandRelativeDir") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir.parent).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    cdCommandRunner.run(List(tempDir.name), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
    assert(environment.currentDir isSame tempDir)
  }

  test("cdCommandNoArgs") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    cdCommandRunner.run(List(), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
    assert(environment.currentDir isSame Path(System.getProperty("user.home")))
  }

  test("cdCommandTooManyArgs") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    val caught =
      intercept[Exception] {
        cdCommandRunner.run(List("179", "da", "best"), environment, IOEnvironment(inputStream, printStream))
      }
    assert(caught.getMessage == "Too many arguments")
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
  }

  test("cdCommandNonExisting") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    val caught =
      intercept[FileNotFoundException] {
        cdCommandRunner.run(List("179/da/best"), environment, IOEnvironment(inputStream, printStream))
      }
    assert(caught.getMessage == "Location not found")
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
  }

  test("cdCommandNonDirectory") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))
    val filename = "definitelyNotDir.txt"
    val tempFile = tempDir.resolve(filename)
    tempFile.createFile(failIfExists = false)

    val cdCommandRunner = new CdCommandRunner
    val environment = Environment(tempDir).registerCommandRunner(cdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    val caught =
      intercept[FileNotFoundException] {
        cdCommandRunner.run(List(tempFile.toString()), environment, IOEnvironment(inputStream, printStream))
      }
    assert(caught.getMessage == "Location is not a directory")
    printStream.flush()
    assert(outputStream.toByteArray.isEmpty)
  }
}