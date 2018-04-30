package ru.spbau.roguelike.controller

import org.codetome.zircon.api.input.{Input, InputType}
import ru.spbau.roguelike.model.WorldState

class InputListenerImpl(private val worldState: WorldState) extends InputListener {
  override def accept(input: Input): Unit = {
    if (input.isKeyStroke) {
      val keyStroke = input.asKeyStroke()
      if (keyStroke.getCharacter.toLower == 'q') {
        System.exit(0)
      }
      if (worldState.getIsInInventory) {
        keyStroke.getInputType match {
          case InputType.ArrowDown =>
            worldState.moveDownInInventory()
          case InputType.ArrowUp =>
            worldState.moveUpInInventory()
          case InputType.Character => keyStroke.getCharacter.toLower match {
            case 'i' =>
              worldState.changeIsInInventoryTo(false)
            case _ =>
          }
          case _ =>
        }
      } else {
        keyStroke.getInputType match {
          case InputType.ArrowUp =>
            worldState.moveUp()
          case InputType.ArrowDown =>
            worldState.moveDown()
          case InputType.ArrowLeft =>
            worldState.moveLeft()
          case InputType.ArrowRight =>
            worldState.moveRight()
          case InputType.Character => keyStroke.getCharacter.toLower match {
            case 'i' =>
              worldState.changeIsInInventoryTo(true)
            case _ =>
          }
          case _ =>
        }
      }
    }
  }
}
