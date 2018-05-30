package ru.spbau.roguelike.model.characters

/** Represents a character that takes up a position on the map. */
trait PositionalCharacter {
  /** Property that stands for x-coordinate. */
  val posX: Int

  /** Property that stands for y-coordinate. */
  val posY: Int

  /** Returns the character moved to the given position. */
  def moveTo(newX: Int, newY: Int): PositionalCharacter
}
