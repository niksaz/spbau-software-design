package ru.spbau.roguelike.view.inventory

import java.util.function.Consumer

import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{GameView, InActionState}

class InventoryScreenListener(
  private val worldState: WorldState,
  private val gameView: GameView,
  private val inventoryScreenController: InventoryScreenController
) extends Consumer[Input] {

  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      input.asKeyStroke().getInputType match {
        case InputType.ArrowDown =>
          inventoryScreenController.moveDownInInventory()
        case InputType.ArrowUp =>
          inventoryScreenController.moveUpInInventory()
        case InputType.Enter =>
          worldState.equipItemWithIndex(inventoryScreenController.getInventoryPosition)
        case InputType.Character => keyStroke.getCharacter.toLower match {
          case 'i' =>
            gameView.changeGameViewStateTo(InActionState())
          case _ =>
        }
        case _ =>
      }
    }
  }
}
