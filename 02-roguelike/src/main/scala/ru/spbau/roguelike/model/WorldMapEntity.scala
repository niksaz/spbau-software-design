package ru.spbau.roguelike.model

abstract class WorldMapEntity {
  def isPassable: Boolean
}

case class Wall() extends WorldMapEntity {
  override def isPassable: Boolean = false
}

case class Floor() extends WorldMapEntity {
  override def isPassable: Boolean = true
}
