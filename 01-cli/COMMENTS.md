# Comments

## Architecture 

- Things I liked
    
    - architecture turned out to be like mine, so it was easy to understand
    - logical package distribution (mostly)    
    - Scala is JVM-based and interoperable with Java so it can be easily built and used on different platforms (not about architecture, but still remarkable)

- and did not
   
    - Word and WordPart seem more like lexical units to me, rather than they should be in model package
    - I would have separated lexer and parser because of the different kind of work they normally do
    - don't really see the need for Cli and CliIO
    - had to change previously immutable Environment to allow updating the working directory (in my opinion it would be better if CommandRunner's could return updated environment)
 
As I said before, it would be better to:

- move Word and WordPart to lexical
- extract parsing, Command, CommandSequence to different package
- so the resulting pipeline would look like `[REPL input] --text-> [lexer] --Words-> [parser] --CommandSequence-> [process] --output-> [REPL output]`
- and make CommandRunner::run return something like `RunResult <- RunFailure(cause), RunSuccess(newEnvironment)` rather than just Unit 
