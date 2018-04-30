package ru.spbau.roguelike.model

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val worldMap: WorldMap,
  private val character: PlayerCharacter,
  private var posX: Int,
  private var posY: Int) {

  private val width: Int = worldMap.width
  private val height: Int = worldMap.height

  private var isInInventory: Boolean = false
  private var inventoryPosition = 0

  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  def getWorldMap: WorldMap = worldMap
  def getCharacterX: Int = posX
  def getCharacterY: Int = posY

  def getCharacter: PlayerCharacter = character

  def getIsInInventory: Boolean = isInInventory
  def getInventoryPosition: Int = inventoryPosition

  def moveUpInInventory(): Unit = {
    inventoryPosition -= 1
    if (inventoryPosition < 0) {
      inventoryPosition += character.getItems.size
    }
    notifyChangeListeners()
  }

  def moveDownInInventory(): Unit = {
    inventoryPosition += 1
    if (inventoryPosition == character.getItems.size) {
      inventoryPosition = 0
    }
    notifyChangeListeners()
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

  def addChangeListener(changeListener: WorldStateChangeListener): Unit = {
    changeListeners.append(changeListener)
  }

  def notifyChangeListeners(): Unit = {
    changeListeners.foreach(_.receiveWorldState(this))
  }
}

object WorldState {
  def apply(width: Int, height: Int): WorldState = {
    val generator: Random = new Random()
    val worldMap = new WorldMap(width, height)
    val character = new PlayerCharacter
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 10), CharacterBodyPart.Hands))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 10), CharacterBodyPart.Hands))
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(width)
      posY = generator.nextInt(height)
    } while (!worldMap.getEntityAt(posX, posY).isPassable)
    new WorldState(worldMap, character, posX, posY)
  }
}
