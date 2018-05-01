package ru.spbau.roguelike.model.character

trait PositionalCharacter {
  val posX: Int
  val posY: Int

  def moveTo(newX: Int, newY: Int): PositionalCharacter
}
