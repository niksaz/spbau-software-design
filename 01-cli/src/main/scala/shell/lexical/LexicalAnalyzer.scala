package shell.lexical

import shell.model.CommandSequence

/** A trait for an entity which can analyzing shell.command line content. */
trait LexicalAnalyzer {
  /** Analyzes the shell.command line content. */
  def analyze(line: String): CommandSequence
}
