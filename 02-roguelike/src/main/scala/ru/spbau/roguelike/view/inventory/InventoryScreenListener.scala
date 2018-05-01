package ru.spbau.roguelike.view.inventory

import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{AbstractScreenListener, GameView, InActionState}

class InventoryScreenListener(
  worldState: WorldState,
  gameView: GameView,
  private val inventoryScreenController: InventoryScreenController
) extends AbstractScreenListener(worldState, gameView) {

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
            gameView.changeGameViewStateTo(InActionState)
          case _ =>
        }
        case _ =>
      }
    }
  }
}
