package shell.command

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

import shell.model.{Environment, IOEnvironment}
import org.scalatest.FunSuite

import scala.reflect.io.Path

class PwdCommandRunnerTest extends FunSuite {
  test("pwdCommand") {
    val pwdCommandRunner = new PwdCommandRunner
    val environment =
      Environment(Path("/Users/niksaz/University/spbau-software-design"))
        .registerCommandRunner(pwdCommandRunner)
    val inputStream = new ByteArrayInputStream("".getBytes)
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)
    pwdCommandRunner.run(List(), environment, IOEnvironment(inputStream, printStream))
    printStream.flush()
    val expected = "/Users/niksaz/University/spbau-software-design\n".getBytes
    assert(outputStream.toByteArray sameElements expected)
  }
}