package shell.process

import java.io._

import shell.model.{CommandSequence, Environment, IOEnvironment}

import scala.collection.mutable
import scala.sys.process._

/** [[CommandProcessor]] which processes shell.command sequentially. */
class SequentialCommandProcessor extends CommandProcessor {
  override def processCommandSequence(
      commandSequence: CommandSequence,
      initialEnvironment: Environment,
      ioEnvironment: IOEnvironment): ProcessingResult = {
    var inputStream = ioEnvironment.inputStream
    var environment = initialEnvironment
    commandSequence.commands.zipWithIndex.foreach { case (command, index) =>
      val args = mutable.ListBuffer[String]()
      command.words.foreach { word =>
        val argBuilder = new mutable.StringBuilder
        for (wordPart <- word.parts) {
          argBuilder.append(wordPart.resolve(environment))
        }
        args.append(argBuilder.toString())
      }
      val outputByteStream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val printStream =
        if (index + 1 == commandSequence.length)
          ioEnvironment.printStream else new PrintStream(outputByteStream)
      val result = processCommand(args.toList, environment, IOEnvironment(inputStream, printStream))
      if (result.shouldExit) {
        return result
      }
      environment = result.environment
      if (index + 1 != commandSequence.length) {
        printStream.flush()
        inputStream = new ByteArrayInputStream(outputByteStream.toByteArray)
      }
    }
    ProcessingResult(environment, shouldExit = false)
  }

  private def processCommand(
      args: List[String],
      environment: Environment,
      ioEnvironment: IOEnvironment): ProcessingResult = {
    val defaultProcessingResult = ProcessingResult(environment, shouldExit = false)
    if (args.isEmpty) {
      return defaultProcessingResult
    }
    val (head, tail) = (args.head, args.tail)
    if (head == "exit") {
      return ProcessingResult(environment, shouldExit = true)
    }
    if (head.contains('=')) {
      val equalsIndex = head.indexOf('=')
      val leftPart = head.substring(0, equalsIndex)
      val rightPart = head.substring(equalsIndex + 1)
      return processCommand(tail, environment.updateVariable(leftPart, rightPart), ioEnvironment)
    }
    environment.commandToRunners.foreach { case (name, commandRunner) =>
      if (name == args.head) {
        commandRunner.run(args.tail, environment, ioEnvironment)
        return defaultProcessingResult
      }
    }
    delegateToExternalProcess(args, environment, ioEnvironment)
    defaultProcessingResult
  }

  private def delegateToExternalProcess(
      args: List[String], environment: Environment, ioEnvironment: IOEnvironment): Unit = {
    def rewriteInputToOutput(outputStream: OutputStream): Unit = {
      val inputStream = ioEnvironment.inputStream
      var byteRead: Int = 0
      while ({byteRead = inputStream.read(); byteRead != -1}) {
        outputStream.write(byteRead)
      }
      outputStream.close()
    }
    def redirectToOutputFromInput(outputStream: OutputStream)(inputStream: InputStream): Unit = {
      var byteRead: Int = 0
      while ({byteRead = inputStream.read(); byteRead != -1}) {
        outputStream.write(byteRead)
      }
      inputStream.close()
    }
    val processIO = new ProcessIO(
      rewriteInputToOutput,
      redirectToOutputFromInput(ioEnvironment.printStream),
      redirectToOutputFromInput(stderr))
    val processBuilder = Process(args.mkString(" "), new File(environment.currentDir.toString()))
    val process = processBuilder.run(processIO)
    process.exitValue()
  }
}