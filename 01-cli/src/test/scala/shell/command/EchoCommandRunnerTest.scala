package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import shell.model.{Environment, IOEnvironment}
import org.scalatest.FunSuite
import shell.Converter

import scala.reflect.io.Path

class EchoCommandRunnerTest extends FunSuite {
  test("echoCommand") {
    val echoCommandRunner = new EchoCommandRunner
    val environment = Environment(Path("")).registerCommandRunner(echoCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    echoCommandRunner
      .run(List("hello", "world"), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expectedBytes = Converter.getLineBytes(endWithSeparator = true, "hello", "world")
    assert(outputStream.toByteArray sameElements expectedBytes)
  }
}
