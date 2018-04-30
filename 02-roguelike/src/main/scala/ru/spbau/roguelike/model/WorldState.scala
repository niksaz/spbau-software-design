package ru.spbau.roguelike.model

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val width: Int,
  private val height: Int,
  private val worldMap: WorldMap,
  private var posX: Int,
  private var posY: Int) {

  private val character = new PlayerCharacter

  private var isInInventory: Boolean = false

  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  def addChangeListener(changeListener: WorldStateChangeListener): Unit = {
    changeListeners.append(changeListener)
  }

  def getCharacterX: Int = posX
  def getCharacterY: Int = posY

  def getWorldMap: WorldMap = worldMap

  def getIsInInventory: Boolean = isInInventory

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

  def changeIsInInventoryTo(isInInventory: Boolean): Unit = {
    this.isInInventory = isInInventory
    notifyChangeListeners()
  }

  private def updatePosByDelta(deltaX: Int, deltaY: Int): Unit = {
    val newX = posX + deltaX
    val newY = posY + deltaY
    if (0 <= newX && newX < width && 0 <= newY && newY < height &&
        worldMap.getEntityAt(newX, newY).isPassable) {
      posX = newX
      posY = newY
    }
    notifyChangeListeners()
  }

  def notifyChangeListeners(): Unit = {
    changeListeners.foreach(_.receiveWorldState(this))
  }
}

object WorldState {
  def apply(width: Int, height: Int): WorldState = {
    val generator: Random = new Random()
    val worldMap = new WorldMap(width, height)
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(width)
      posY = generator.nextInt(height)
    } while (!worldMap.getEntityAt(posX, posY).isPassable)
    new WorldState(width, height, worldMap, posX, posY)
  }
}
