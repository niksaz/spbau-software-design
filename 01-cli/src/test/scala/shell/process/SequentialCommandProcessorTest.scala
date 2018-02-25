package shell.process

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.util.Scanner

import shell.command.{CommandRunner, WcCommandRunner}
import shell.model._
import org.scalatest.FunSuite

import scala.collection.mutable
import scala.reflect.io.Path

class SequentialCommandProcessorTest extends FunSuite {
  test("processorPipelining") {
    val inputGot = mutable.ListBuffer[String]()
    val commandRunner = new CommandRunner("surprise") {
      override def run(
          args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
        val scanner = new Scanner(ioEnvironment.inputStream)
        while (scanner.hasNext) {
          val line = scanner.nextLine()
          inputGot.append(line)
          ioEnvironment.printStream.println(s"$line!")
        }
      }
    }
    val environment = Environment(Path("")).registerCommandRunner(commandRunner)
    val sequentialCommandProcessor = new SequentialCommandProcessor
    val inputStream = new ByteArrayInputStream("light\n".getBytes)
    val byteArrayOutputStream = new ByteArrayOutputStream
    val printStream = new PrintStream(byteArrayOutputStream)
    sequentialCommandProcessor
      .processCommandSequence(
        CommandSequence(List(
          Command(List(Word(List(StringPart("surprise"))))),
          Command(List(Word(List(StringPart("surprise"))))),
          Command(List(Word(List(StringPart("surprise"))))))),
        environment,
        IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(inputGot.toList == List("light", "light!", "light!!"))
    assert(byteArrayOutputStream.toByteArray sameElements "light!!!\n".getBytes)
  }

  test("pipeliningExternalCommand") {
    val tmpProperty = "java.io.tmpdir"
    val tempDir = Path(System.getProperty(tmpProperty))
    val tempInnerDir = tempDir.resolve("pipeliningExternalCommandDir")
    tempInnerDir.createDirectory(force = true, failIfExists = false)
    tempInnerDir.resolve("file1").createFile(failIfExists = false)
    tempInnerDir.resolve("file2").createFile(failIfExists = false)
    tempInnerDir.resolve("file3").createFile(failIfExists = false)

    val wcCommandRunner = new WcCommandRunner
    val environment = Environment(tempInnerDir).registerCommandRunner(wcCommandRunner)

    val sequentialCommandProcessor = new SequentialCommandProcessor
    val inputStream = new ByteArrayInputStream("".getBytes)
    val byteArrayOutputStream = new ByteArrayOutputStream
    val printStream = new PrintStream(byteArrayOutputStream)
    sequentialCommandProcessor
      .processCommandSequence(
        CommandSequence(List(
          Command(List(Word(List(StringPart("ls"))))),
          Command(List(Word(List(StringPart("wc"))))))),
        environment,
        IOEnvironment(inputStream, printStream))
    printStream.flush()
    assert(byteArrayOutputStream.toByteArray sameElements "3 3 18\n".getBytes)
  }
}