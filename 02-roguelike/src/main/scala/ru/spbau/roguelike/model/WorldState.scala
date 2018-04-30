package ru.spbau.roguelike.model

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val worldMap: WorldMap,
  private val character: PlayerCharacter,
  private var posX: Int,
  private var posY: Int) {

  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  def getWorldMap: WorldMap = worldMap
  def getCharacterX: Int = posX
  def getCharacterY: Int = posY

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
    val newX = posX + deltaX
    val newY = posY + deltaY
    if (worldMap.isCellInsideMap(newX, newY) && worldMap.getEntityAt(newX, newY).isPassable) {
      posX = newX
      posY = newY
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
    val worldMap = new WorldMap(width, height)
    val character = new PlayerCharacter
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), CharacterBodyPart.Hands))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), CharacterBodyPart.Body))
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), CharacterBodyPart.Hands))
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(width)
      posY = generator.nextInt(height)
    } while (!worldMap.getEntityAt(posX, posY).isPassable)
    new WorldState(worldMap, character, posX, posY)
  }
}
