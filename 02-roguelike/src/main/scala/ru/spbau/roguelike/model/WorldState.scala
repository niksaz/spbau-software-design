package ru.spbau.roguelike.model

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val terrainMap: TerrainMap,
  private var character: PlayerCharacter
) {
  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  def getTerrainMap: TerrainMap = terrainMap

  def getCharacter: PlayerCharacter = character

  def equipItemWithIndex(itemIndex: Int): Unit = {
    character.invertIsEquippedItemWithIndex(itemIndex)
    notifyChangeListeners()
  }

  def moveCharacterUp(): Unit = {
    moveCharacterByDelta(0, -1)
  }

  def moveCharacterDown(): Unit = {
    moveCharacterByDelta(0, 1)
  }

  def moveCharacterLeft(): Unit = {
    moveCharacterByDelta(-1, 0)
  }

  def moveCharacterRight(): Unit = {
    moveCharacterByDelta(1, 0)
  }

  private def moveCharacterByDelta(deltaX: Int, deltaY: Int): Unit = {
    val newX = character.posX + deltaX
    val newY = character.posY + deltaY
    if (terrainMap.isCellInsideMap(newX, newY) && terrainMap.getEntityAt(newX, newY).isPassable) {
      character = character.moveTo(newX, newY)
    }
    notifyChangeListeners()
  }

  def addChangeListener(changeListener: WorldStateChangeListener): Unit = {
    changeListeners.append(changeListener)
  }

  def notifyChangeListeners(): Unit = {
    changeListeners.foreach(_.worldStateUpdated())
  }
}

object WorldState {
  def apply(width: Int, height: Int): WorldState = {
    val generator: Random = new Random()
    val terrainMap = new TerrainMap(width, height)
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(width)
      posY = generator.nextInt(height)
    } while (!terrainMap.getEntityAt(posX, posY).isPassable)
    val character = PlayerCharacter(posX, posY)
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), HandsItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), HandsItemSlot))
    new WorldState(terrainMap, character)
  }
}
