package ru.spbau.roguelike.view.lost

import com.typesafe.scalalogging.Logger
import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{AbstractScreenListener, GameView}

/** The keyboard listener when the player has lost. */
class LostScreenListener(
  worldState: WorldState,
  gameView: GameView
) extends AbstractScreenListener(worldState, gameView) {

  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      if (keyStroke.getInputType == InputType.Character && keyStroke.getCharacter.toUpper == 'Q') {
        LostScreenListener.logger.info("'Q' pressed")
        System.exit(0)
      }
    }
  }
}

private object LostScreenListener {
  private val logger = Logger(classOf[LostScreenListener])
}
