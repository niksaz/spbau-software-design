package ru.spbau.roguelike.model.terrain

import scala.util.Random

/**
  * Represents the terrain of the world's map.
  * Constructor generates a random map of given width and height with walls around the perimeter.
  */
class TerrainMap(val width: Int, val height: Int) {
  private val entities = Array.ofDim[TerrainMapEntity](width, height)

  {
    val generator = new Random
    for (column <- 0 until width) {
      for (row <- 0 until height) {
        entities(column)(row) =
          if (column == 0 || column + 1 == width || row == 0 || row + 1 == height) {
            WallMapEntity
          } else {
            if (generator.nextInt(4) == 0) WallMapEntity else FloorMapEntity
          }
      }
    }
  }

  /** Returns a [[TerrainMapEntity]] at the given coordinates. */
  def getEntityAt(column: Int, row: Int): TerrainMapEntity = entities(column)(row)

  /** Returns whether the character can be moved to the given coordinates. */
  def isPassable(x: Int, y: Int): Boolean =
    0 <= x && x < width && 0 <= y && y < height && entities(x)(y).isPassable
}
