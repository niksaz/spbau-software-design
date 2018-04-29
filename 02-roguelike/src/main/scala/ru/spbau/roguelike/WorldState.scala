package ru.spbau.roguelike

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.graphics.Layer

import scala.util.Random

class WorldState(private val width: Int, private val height: Int) {

  private val map: Array[Array[Char]] = new Array(width)
  private val generator: Random = new Random()

  private var posX: Int = generator.nextInt(width)
  private var posY: Int = generator.nextInt(height)

  {
    var i = 0
    while (i < map.length) {
      map(i) = new Array(height)
      i += 1
    }
    for (i <- 0 until width) {
      for (j <- 0 until height) {
        map(i)(j) = if (generator.nextInt(4) == 0) '#' else '.'
      }
    }
    map(posX)(posY) = '*'
  }

  def updateOverlay(overlay: Layer): Unit = {
    for (row <- 0 until height) {
      for (column <- 0 until width) {
        if (column == posX && row == posY) {
          overlay.setBackgroundColor(ANSITextColor.GREEN)
        }
        overlay.setCharacterAt(Position.of(column, row), map(column)(row))
        overlay.resetColorsAndModifiers()
      }
    }
  }

  def moveUp(): Unit = {
    updatePosByDelta(0, -1)
  }

  def moveDown(): Unit = {
    updatePosByDelta(0, 1)
  }

  def moveLeft(): Unit = {
    updatePosByDelta(-1, 0)
  }

  def moveRight(): Unit = {
    updatePosByDelta(1, 0)
  }

  private def updatePosByDelta(deltaX: Int, deltaY: Int): Unit = {
    val newX = posX + deltaX
    val newY = posY + deltaY
    if (0 <= newX && newX < width && 0 <= newY && newY < height) {
      if (map(newX)(newY) != '#') {
        map(posX)(posY) = '.'
        map(newX)(newY) = '*'
        posX = newX
        posY = newY
      }
    }
  }
}