package ru.spbau.roguelike.view.action

import java.util.function.Consumer

import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{GameView, InInventoryState}

class ActionScreenListener(
  private val worldState: WorldState,
  private val gameView: GameView
) extends Consumer[Input] {

  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      if (keyStroke.getCharacter.toLower == 'q') {
        System.exit(0)
      }
      keyStroke.getInputType match {
        case InputType.ArrowUp =>
          worldState.moveCharacterUp()
        case InputType.ArrowDown =>
          worldState.moveCharacterDown()
        case InputType.ArrowLeft =>
          worldState.moveCharacterLeft()
        case InputType.ArrowRight =>
          worldState.moveCharacterRight()
        case InputType.Character => keyStroke.getCharacter.toLower match {
          case 'i' =>
            gameView.changeGameViewStateTo(InInventoryState())
          case _ =>
        }
        case _ =>
      }
    }
  }
}
