package ru.spbau.roguelike.view.map

import com.typesafe.scalalogging.Logger
import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{AbstractScreenListener, GameView, InInventoryState, GameLostState}

/** The keyboard listener when the players is on the map screen. */
class MapScreenListener(
  worldState: WorldState,
  gameView: GameView
) extends AbstractScreenListener(worldState, gameView) {

  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      keyStroke.getInputType match {
        case InputType.ArrowUp =>
          MapScreenListener.logger.info("ArrowUp pressed")
          worldState.moveCharacterUp()
        case InputType.ArrowDown =>
          MapScreenListener.logger.info("ArrowDown pressed")
          worldState.moveCharacterDown()
        case InputType.ArrowLeft =>
          MapScreenListener.logger.info("ArrowLeft pressed")
          worldState.moveCharacterLeft()
        case InputType.ArrowRight =>
          MapScreenListener.logger.info("ArrowRight pressed")
          worldState.moveCharacterRight()
        case InputType.Character => keyStroke.getCharacter.toLower match {
          case 'i' =>
            MapScreenListener.logger.info("'I' pressed")
            gameView.changeGameViewStateTo(InInventoryState)
          case 'q' =>
            MapScreenListener.logger.info("'Q' pressed")
            System.exit(0)
          case _ =>
        }
        case _ =>
      }
      if (worldState.getCharacter.getCurrentHealth == 0) {
        gameView.changeGameViewStateTo(GameLostState)
      }
    }
  }
}

private object MapScreenListener {
  private val logger = Logger(classOf[MapScreenListener])
}
