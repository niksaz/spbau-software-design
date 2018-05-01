package ru.spbau.roguelike.model

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val terrainMap: TerrainMap,
  private var character: PlayerCharacter,
  private var mobs: List[MobCharacter],
) {
  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  def getTerrainMap: TerrainMap = terrainMap

  def getCharacter: PlayerCharacter = character

  def getMobs: List[MobCharacter] = mobs

  def equipItemWithIndex(itemIndex: Int): Unit = {
    character.invertIsEquippedItemWithIndex(itemIndex)
    notifyChangeListeners()
  }

  def moveCharacterUp(): Unit = {
    nextTimeStepWithCharacterDelta(0, -1)
  }

  def moveCharacterDown(): Unit = {
    nextTimeStepWithCharacterDelta(0, 1)
  }

  def moveCharacterLeft(): Unit = {
    nextTimeStepWithCharacterDelta(-1, 0)
  }

  def moveCharacterRight(): Unit = {
    nextTimeStepWithCharacterDelta(1, 0)
  }

  private def nextTimeStepWithCharacterDelta(charDeltaX: Int, charDeltaY: Int): Unit = {
    val charNewX = character.posX + charDeltaX
    val charNewY = character.posY + charDeltaY
    character = if (terrainMap.isPassable(charNewX, charNewY)) {
      character.moveTo(charNewX, charNewY)
    } else {
      character
    }
    character = character.reduceHealth(25)
    mobs = mobs.map { mob =>
      val mobDir = WorldState.DIRS(WorldState.generator.nextInt(4))
      val mobNewX = mob.posX + mobDir._1
      val mobNewY = mob.posY + mobDir._2
      if (terrainMap.isPassable(mobNewX, mobNewY)) {
        mob.moveTo(mobNewX, mobNewY)
      } else {
        mob
      }
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
  private val DIRS = List((1, 0), (-1, 0), (0, 1), (0, -1))

  private val generator: Random = new Random()

  def apply(width: Int, height: Int): WorldState = {
    val terrainMap = new TerrainMap(width, height)
    val (characterX, characterY) = generatePassablePosition(terrainMap)
    val character = PlayerCharacter(characterX, characterY)
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), HandsItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden armor", CombatStats(0, 2, 0), BodyItemSlot))
    character.addItem(Item("Wooden sword", CombatStats(0, 0, 5), HandsItemSlot))
    val mobs = mutable.ListBuffer[MobCharacter]()
    (0 until 3).foreach { _ =>
      val (mobX, mobY) = generatePassablePosition(terrainMap)
      mobs.append(MobCharacter(mobX, mobY))
    }
    new WorldState(terrainMap, character, mobs.toList)
  }

  def generatePassablePosition(terrainMap: TerrainMap): (Int, Int) = {
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(terrainMap.width)
      posY = generator.nextInt(terrainMap.height)
    } while (!terrainMap.getEntityAt(posX, posY).isPassable)
    (posX, posY)
  }
}
