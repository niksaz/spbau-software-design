package model

/* Represents a part of the word in the command line. */
sealed trait WordPart {
  /** Gets plain text. */
  def text: String

  /** Resolves text through [[VariableSupplier]]. */
  def resolve(variableSupplier: VariableSupplier): String
}

/** Part of the word that can be resolved. */
case class StringPart(text: String) extends WordPart {
  override def resolve(variableSupplier: VariableSupplier): String = {
    //TODO
    text
  }
}

/** Part of the word which should not be resolved. */
case class FullQuotedPart(text: String) extends WordPart {
  override def resolve(variableSupplier: VariableSupplier): String = text
}