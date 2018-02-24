package process

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.util.Scanner

import command.CommandRunner
import model._
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
}