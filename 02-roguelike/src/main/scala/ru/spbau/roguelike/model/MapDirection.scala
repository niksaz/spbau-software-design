package ru.spbau.roguelike.model

/** Contains directions of moving around the map. */
object MapDirection {

  /** Trait that stands for a direction of moving around the map. */
  sealed trait Direction {
    val deltaX: Int
    val deltaY: Int
  }

  /** Represents a direction of moving to the north. */
  case object NorthDirection extends Direction {
    override val deltaX: Int = 0
    override val deltaY: Int = -1
  }

  /** Represents a direction of moving to the south. */
  case object SouthDirection extends Direction {
    override val deltaX: Int = 0
    override val deltaY: Int = 1
  }

  /** Represents a direction of moving to the west. */
  case object WestDirection extends Direction {
    override val deltaX: Int = -1
    override val deltaY: Int = 0
  }

  /** Represents a direction of moving to the east. */
  case object EastDirection extends Direction {
    override val deltaX: Int = 1
    override val deltaY: Int = 0
  }

  val directions = Seq(NorthDirection, SouthDirection, WestDirection, EastDirection)
}
