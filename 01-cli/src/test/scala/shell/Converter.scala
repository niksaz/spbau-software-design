package shell

object Converter {
  /** Return bytes for [[String]]s joined by the system line separator. */
  def getLineBytes(endWithSeparator: Boolean, strings: String*): Array[Byte] = {
    val sep = System.lineSeparator()
    strings.mkString("", sep, if (endWithSeparator) sep else "").getBytes
  }
}