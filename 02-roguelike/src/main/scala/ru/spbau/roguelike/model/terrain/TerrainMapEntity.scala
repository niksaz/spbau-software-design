package ru.spbau.roguelike.model.terrain

sealed trait TerrainMapEntity {
  def isPassable: Boolean
}

case object Wall extends TerrainMapEntity {
  override def isPassable: Boolean = false
}

case object Floor extends TerrainMapEntity {
  override def isPassable: Boolean = true
}
