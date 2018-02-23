package lexical

import model._
import org.scalatest.FunSuite

class LexerTest extends FunSuite {
  test("analyzePipedCommand") {
    val lexer = new Lexer
    val sequence = lexer.analyze("echo \"$x\"12 | ls 'input.txt'")
    val expectedSequence =
      CommandSequence(List(
        Command(List(
          Word(List(StringPart("echo"))),
          Word(List(StringPart("$x"), StringPart("12"))))),
        Command(List(
          Word(List(StringPart("ls"))),
          Word(List(FullQuotedPart("input.txt")))))))
    assert(sequence == expectedSequence)
  }

  test("unbalancedDoubleQuote") {
    val lexer = new Lexer
    intercept[LexicalParsingException] {
      lexer.analyze("echo \"words.txt")
    }
  }

  test("unbalancedSingleQuote") {
    val lexer = new Lexer
    intercept[LexicalParsingException] {
      lexer.analyze("cat 'output.txt' 'expected.txt")
    }
  }
}