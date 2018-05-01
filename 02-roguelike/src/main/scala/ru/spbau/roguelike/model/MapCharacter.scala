package ru.spbau.roguelike.model

trait MapCharacter {
  val posX: Int
  val posY: Int

  def moveTo(newX: Int, newY: Int): MapCharacter
}
