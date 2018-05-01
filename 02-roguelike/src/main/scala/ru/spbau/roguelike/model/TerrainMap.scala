package ru.spbau.roguelike.model

import scala.util.Random

class TerrainMap(val width: Int, val height: Int) {

  private val entities = Array.ofDim[TerrainMapEntity](width, height)

  {
    val generator = new Random
    for (column <- 0 until width) {
      for (row <- 0 until height) {
        entities(column)(row) =
          if (column == 0 || column + 1 == width || row == 0 || row + 1 == height) {
            Wall()
          } else {
            if (generator.nextInt(4) == 0) Wall() else Floor()
          }
      }
    }
  }

  def getEntityAt(column: Int, row: Int): TerrainMapEntity = entities(column)(row)

  def isCellInsideMap(x: Int, y: Int): Boolean = 0 <= x && x < width && 0 <= y && y < height
}
