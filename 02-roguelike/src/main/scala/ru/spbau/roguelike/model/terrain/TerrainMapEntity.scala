package ru.spbau.roguelike.model.terrain

abstract class TerrainMapEntity {
  def isPassable: Boolean
}

case class Wall() extends TerrainMapEntity {
  override def isPassable: Boolean = false
}

case class Floor() extends TerrainMapEntity {
  override def isPassable: Boolean = true
}
