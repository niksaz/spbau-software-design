package lexical

import model.CommandSequence

/** A trait for an entity which can analyzing command line content. */
trait LexicalAnalyzer {
  /** Analyzes the command line content. */
  def analyze(line: String): CommandSequence
}
