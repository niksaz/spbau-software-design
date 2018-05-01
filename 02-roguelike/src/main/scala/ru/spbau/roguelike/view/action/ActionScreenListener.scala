package ru.spbau.roguelike.view.action

import com.typesafe.scalalogging.Logger
import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{AbstractScreenListener, GameView, InInventoryState, InLostState}

/** The keyboard listener when the players is on the map screen. */
class ActionScreenListener(
  worldState: WorldState,
  gameView: GameView
) extends AbstractScreenListener(worldState, gameView) {

  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      keyStroke.getInputType match {
        case InputType.ArrowUp =>
          ActionScreenListener.logger.info("ArrowUp pressed")
          worldState.moveCharacterUp()
        case InputType.ArrowDown =>
          ActionScreenListener.logger.info("ArrowDown pressed")
          worldState.moveCharacterDown()
        case InputType.ArrowLeft =>
          ActionScreenListener.logger.info("ArrowLeft pressed")
          worldState.moveCharacterLeft()
        case InputType.ArrowRight =>
          ActionScreenListener.logger.info("ArrowRight pressed")
          worldState.moveCharacterRight()
        case InputType.Character => keyStroke.getCharacter.toLower match {
          case 'i' =>
            ActionScreenListener.logger.info("'I' pressed")
            gameView.changeGameViewStateTo(InInventoryState)
          case 'q' =>
            ActionScreenListener.logger.info("'Q' pressed")
            System.exit(0)
          case _ =>
        }
        case _ =>
      }
      if (worldState.getCharacter.getCurrentHealth == 0) {
        gameView.changeGameViewStateTo(InLostState)
      }
    }
  }
}

private object ActionScreenListener {
  private val logger = Logger(classOf[ActionScreenListener])
}
