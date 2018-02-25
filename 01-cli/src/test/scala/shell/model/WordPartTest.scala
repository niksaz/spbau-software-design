package shell.model

import org.scalatest.FunSuite

class WordPartTest extends FunSuite {
  test("stringPartResolve") {
    val resolvablePart = StringPart("$x pro$y")
    val variableSupplier = new VariableSupplier {
      override def retrieveVariable(variableName: String): String =
        if (variableName == "x") "world" else ""
    }
    val result = resolvablePart.resolve(variableSupplier)
    assert(result == "world pro")
  }

  test("fullQuotedPartResolve") {
    val resolvablePart = FullQuotedPart("$x pro$y")
    val variableSupplier = new VariableSupplier {
      override def retrieveVariable(variableName: String): String = "?"
    }
    val result = resolvablePart.resolve(variableSupplier)
    assert(result == "$x pro$y")
  }
}