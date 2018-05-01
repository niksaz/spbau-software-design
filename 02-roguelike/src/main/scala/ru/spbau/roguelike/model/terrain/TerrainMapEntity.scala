package ru.spbau.roguelike.model.terrain

/** Represents an entity for a [[TerrainMap]]. */
sealed trait TerrainMapEntity {
  /** Returns whether this type of terrain is passable by characters. */
  def isPassable: Boolean
}

/** Represents a wall cell on the map. */
case object WallMapEntity extends TerrainMapEntity {
  override def isPassable: Boolean = false
}

/** Represents a floor cell on the map. */
case object FloorMapEntity extends TerrainMapEntity {
  override def isPassable: Boolean = true
}
