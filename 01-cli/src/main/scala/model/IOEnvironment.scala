package model

import java.io.{InputStream, OutputStream}

/** Represents stdin's and stdout's streams. */
case class IOEnvironment(inputStream: InputStream, outputStream: OutputStream)