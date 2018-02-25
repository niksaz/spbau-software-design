package shell.lexical

import shell.model._

import scala.collection.mutable

/** [[LexicalAnalyzer]] which extracts logical words. */
class Lexer extends LexicalAnalyzer {
  private val commandList = mutable.ListBuffer[Command]()
  private val wordList = mutable.ListBuffer[Word]()
  private val wordPartList = mutable.ListBuffer[WordPart]()

  override def analyze(line: String): CommandSequence = {
    commandList.clear()
    wordList.clear()
    wordPartList.clear()
    var i = 0
    while (i < line.length) {
      line(i) match {
        case ' ' =>
          flushWordPartList()
          i += 1
        case '|' =>
          flushWordPartList()
          flushWordList()
          i += 1
        case '\'' =>
          i += 1
          var j = i
          while (j < line.length && line(j) != '\'') {
            j += 1
          }
          if (j == line.length) {
            throw new LexicalParsingException("Unmatched single quote")
          }
          wordPartList.append(FullQuotedPart(line.substring(i, j)))
          i = j + 1
        case '"' =>
          i += 1
          var j = i
          while (j < line.length && line(j) != '"') {
            j += 1
          }
          if (j == line.length) {
            throw new LexicalParsingException("Unmatched double quote")
          }
          wordPartList.append(StringPart(line.substring(i, j)))
          i = j + 1
        case _ =>
          var j = i
          while (j < line.length && line(j) != ' ' && line(j) != '|'
              && line(j) != '\'' && line(j) != '"') {
            j += 1
          }
          wordPartList.append(StringPart(line.substring(i, j)))
          i = j
      }
    }
    flushWordPartList()
    flushWordList()
    CommandSequence(commandList.toList)
  }

  private def flushWordPartList(): Unit = {
    if (wordPartList.nonEmpty) {
      wordList.append(Word(wordPartList.toList))
      wordPartList.clear()
    }
  }

  private def flushWordList(): Unit = {
    commandList.append(Command(wordList.toList))
    wordList.clear()
  }
}