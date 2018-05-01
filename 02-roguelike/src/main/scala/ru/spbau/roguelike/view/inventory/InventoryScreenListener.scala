package ru.spbau.roguelike.view.inventory

import com.typesafe.scalalogging.Logger
import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.{AbstractScreenListener, GameView, OnMapState}

/** The keyboard listener when the player is in the inventory. */
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
          InventoryScreenListener.logger.info("ArrowDown pressed")
          inventoryScreenController.moveDownInInventory()
        case InputType.ArrowUp =>
          InventoryScreenListener.logger.info("ArrowUp pressed")
          inventoryScreenController.moveUpInInventory()
        case InputType.Enter =>
          InventoryScreenListener.logger.info("Enter pressed")
          worldState.equipItemWithIndex(inventoryScreenController.getInventoryPosition)
        case InputType.Character => keyStroke.getCharacter.toUpper match {
          case 'I' =>
            InventoryScreenListener.logger.info("'I' pressed")
            gameView.changeGameViewStateTo(OnMapState)
          case _ =>
        }
        case _ =>
      }
    }
  }
}

private object InventoryScreenListener {
  private val logger = Logger(classOf[InventoryScreenListener])
}
