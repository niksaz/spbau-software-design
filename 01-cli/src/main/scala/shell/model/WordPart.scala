package shell.model

import scala.collection.mutable

/* Represents a part of the word in the shell.command line. */
sealed trait WordPart {
  /** Gets plain text. */
  def text: String

  /** Resolves text through [[VariableSupplier]]. */
  def resolve(variableSupplier: VariableSupplier): String
}

/** Part of the word that can be resolved. */
case class StringPart(text: String) extends WordPart {
  override def resolve(variableSupplier: VariableSupplier): String = {
    //TODO(niksaz): Introduce right associated $ handling.
    val resolvedText = new mutable.StringBuilder
    var i = 0
    while (i < text.length) {
      if (text(i) != '$') {
        resolvedText.append(text(i))
        i += 1
      } else {
        i += 1
        var j = i
        while (j < text.length && text(j) != ' ') {
          j += 1
        }
        val variableName = text.substring(i, j)
        resolvedText.append(variableSupplier.retrieveVariable(variableName))
        i = j
      }
    }
    resolvedText.toString()
  }
}

/** Part of the word which should not be resolved. */
case class FullQuotedPart(text: String) extends WordPart {
  override def resolve(variableSupplier: VariableSupplier): String = text
}