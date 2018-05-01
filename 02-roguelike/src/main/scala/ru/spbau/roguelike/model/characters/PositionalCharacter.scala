package ru.spbau.roguelike.model.characters

trait PositionalCharacter {
  val posX: Int
  val posY: Int

  def moveTo(newX: Int, newY: Int): PositionalCharacter
}
