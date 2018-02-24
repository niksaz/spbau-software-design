package model

import java.io.{InputStream, PrintStream}

/** Represents stdin's and stdout's streams. */
case class IOEnvironment(inputStream: InputStream, printStream: PrintStream)