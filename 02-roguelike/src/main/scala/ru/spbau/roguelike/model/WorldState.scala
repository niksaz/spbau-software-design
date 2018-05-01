package ru.spbau.roguelike.model

import ru.spbau.roguelike.model.characters._
import ru.spbau.roguelike.model.combat.CombatResolver
import ru.spbau.roguelike.model.items.DropItemGenerator
import ru.spbau.roguelike.model.terrain.TerrainMap

import scala.collection.mutable
import scala.util.Random

class WorldState private (
  private val terrainMap: TerrainMap,
  private var character: PlayerCharacter,
  private var mobs: List[MobCharacter],
) {
  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  private var lastTimeStepMessage = "The world has been created."

  def getLastTimeStepMessage: String = lastTimeStepMessage

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
    lastTimeStepMessage = ""
    var charNewX = character.posX + charDeltaX
    var charNewY = character.posY + charDeltaY
    if (!terrainMap.isPassable(charNewX, charNewY)) {
      charNewX = character.posX
      charNewY = character.posY
    }
    var wasFighting = false
    val newMobs = mutable.ListBuffer[MobCharacter]()
    mobs.foreach { mobInPast =>
      var mob = mobInPast
      val mobDir = WorldState.DIRS(WorldState.generator.nextInt(4))
      var mobNewX = mob.posX + mobDir._1
      var mobNewY = mob.posY + mobDir._2
      if (!terrainMap.isPassable(mobNewX, mobNewY)) {
        mobNewX = mob.posX
        mobNewY = mob.posY
      }
      val sameFinalPos = (mobNewX, mobNewY) == (charNewX, charNewY)
      val swappedPos =
        (mobNewX, mobNewY) == (character.posX, character.posY) &&
        (charNewX, charNewY) == (mob.posX, mob.posY)
      val shouldFightOccur = sameFinalPos || swappedPos
      if (shouldFightOccur) {
        val afterFightChars = WorldState.combatResolver.resolveFight(character, mob)
        character = afterFightChars._1.asInstanceOf[PlayerCharacter]
        mob = afterFightChars._2.asInstanceOf[MobCharacter]
        if (lastTimeStepMessage.isEmpty) {
        }
      } else {
        mob = mob.moveTo(mobNewX, mobNewY)
      }
      wasFighting |= shouldFightOccur
      if (mob.currentHealth == 0) {
        val droppedItem = WorldState.dropItemGenerator.generateDropItem(character, mob)
        lastTimeStepMessage = f"You've defeated a mob!${
          if (droppedItem.nonEmpty) f" It dropped ${droppedItem.get.name}" else ""}"
        if (droppedItem.nonEmpty) {
          character = character.addItem(droppedItem.get)
        }
      } else {
        newMobs.append(mob)
        if (shouldFightOccur) {
          lastTimeStepMessage = f"You've fought a mob. It has ${mob.currentHealth} HP left."
        }
      }
    }
    if (!wasFighting) {
      character = character.moveTo(charNewX, charNewY)
    }
    mobs = newMobs.toList
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
  private val MOBS_TO_SPAWN = 3

  private val generator: Random = new Random()
  private val dropItemGenerator = DropItemGenerator
  private val combatResolver: CombatResolver = CombatResolver()

  def apply(width: Int, height: Int): WorldState = {
    val terrainMap = new TerrainMap(width, height)
    val (characterX, characterY) = generatePassablePosition(terrainMap)
    val character = PlayerCharacter(characterX, characterY)
    val mobs = mutable.ListBuffer[MobCharacter]()
    Range(0, MOBS_TO_SPAWN).foreach { _ =>
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
