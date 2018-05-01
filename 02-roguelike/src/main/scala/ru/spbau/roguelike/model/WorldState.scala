package ru.spbau.roguelike.model

import com.typesafe.scalalogging.Logger
import ru.spbau.roguelike.model.characters._
import ru.spbau.roguelike.model.combat.CombatResolver
import ru.spbau.roguelike.model.items.DropItemGenerator
import ru.spbau.roguelike.model.terrain.TerrainMap

import scala.collection.mutable
import scala.util.Random

/** Encapsulates the state of game's model. */
class WorldState private (
  private val terrainMap: TerrainMap,
  private var character: PlayerCharacter,
  private var mobs: List[MobCharacter],
) {
  private val changeListeners = mutable.ListBuffer[WorldStateChangeListener]()

  private var lastTimeStepMessage = "The world has been created."

  /** Returns the message of that happened during last time step. */
  def getLastTimeStepMessage: String = lastTimeStepMessage

  /** Returns the [[TerrainMap]] in the current state. */
  def getTerrainMap: TerrainMap = terrainMap

  /** Returns the [[PlayerCharacter]] in the current state. */
  def getCharacter: PlayerCharacter = character

  /** Returns the [[MobCharacter]]s in the current state. */
  def getMobs: List[MobCharacter] = mobs

  /** Subscribes [[WorldStateChangeListener]] for further updates of the state. */
  def addChangeListener(changeListener: WorldStateChangeListener): Unit = {
    changeListeners.append(changeListener)
  }

  /**
    * Asks the model to equip the state's character item with the given index.
    * Notifies the listeners after the update.
    */
  def equipItemWithIndex(itemIndex: Int): Unit = {
    character.invertIsEquippedItemWithIndex(itemIndex)
    notifyChangeListeners()
  }

  /**
    * Makes the character move up.
    * Process the next time step and notifies the listeners after the update.
    */
  def moveCharacterUp(): Unit = {
    nextTimeStepWithCharacterDelta((0, -1))
  }

  /**
    * Makes the character move down.
    * Process the next time step and notifies the listeners after the update.
    */
  def moveCharacterDown(): Unit = {
    nextTimeStepWithCharacterDelta((0, 1))
  }

  /**
    * Makes the character move left.
    * Process the next time step and notifies the listeners after the update.
    */
  def moveCharacterLeft(): Unit = {
    nextTimeStepWithCharacterDelta((-1, 0))
  }

  /**
    * Makes the character move right.
    * Process the next time step and notifies the listeners after the update.
    */
  def moveCharacterRight(): Unit = {
    nextTimeStepWithCharacterDelta((1, 0))
  }

  private def nextTimeStepWithCharacterDelta(delta: (Int, Int)): Unit = {
    WorldState.logger.info("New time step")
    WorldState.logger.info(s"Character state is $character")
    lastTimeStepMessage = ""
    val (charNewX, charNewY) = tryToMoveByDelta(character.posX, character.posY, delta)
    var wasInCombat = false
    val newMobs = mutable.ListBuffer[MobCharacter]()
    mobs.foreach { mobInPast =>
      var mob = mobInPast
      val mobDir = WorldState.DIRS(WorldState.generator.nextInt(4))
      val (mobNewX, mobNewY) = tryToMoveByDelta(mob.posX, mob.posY, mobDir)
      val sameFinalPos = (mobNewX, mobNewY) == (charNewX, charNewY)
      val swappedPos =
        (mobNewX, mobNewY) == (character.posX, character.posY) &&
        (charNewX, charNewY) == (mob.posX, mob.posY)
      val shouldCombatOccur = sameFinalPos || swappedPos
      if (shouldCombatOccur) {
        WorldState.logger.info(s"Fighting with mob")
        val afterFightChars = WorldState.combatResolver.resolveCombat(character, mob)
        character = afterFightChars._1.asInstanceOf[PlayerCharacter]
        mob = afterFightChars._2.asInstanceOf[MobCharacter]
      } else {
        WorldState.logger.info(
          s"Mob moving from (${mob.posX}, ${mob.posY}) to ($mobNewX, $mobNewY).")
        mob = mob.moveTo(mobNewX, mobNewY)
      }
      wasInCombat |= shouldCombatOccur
      if (mob.currentHealth == 0) {
        val droppedItem = WorldState.dropItemGenerator.generateDropItem(character, mob)
        lastTimeStepMessage = s"You've defeated a mob!${
          if (droppedItem.nonEmpty) s" It dropped ${droppedItem.get.name}" else ""}"
        WorldState.logger.info(s"$lastTimeStepMessage")
        if (droppedItem.nonEmpty) {
          character = character.addItem(droppedItem.get)
        }
      } else {
        newMobs.append(mob)
        if (shouldCombatOccur) {
          lastTimeStepMessage = s"You've fought a mob. It has ${mob.currentHealth} HP left."
        }
      }
    }
    if (!wasInCombat) {
      WorldState.logger.info(
        s"Character moving from (${character.posX}, ${character.posY}) to ($charNewX, $charNewY).")
      character = character.moveTo(charNewX, charNewY)
    }
    mobs = newMobs.toList
    notifyChangeListeners()
  }

  private def tryToMoveByDelta(posX: Int, posY: Int, delta: (Int, Int)): (Int, Int) = {
    val newPosX = posX + delta._1
    val newPosY = posY + delta._2
    if (terrainMap.isPassable(newPosX, newPosY)) (newPosX, newPosY) else (posX, posY)
  }

  private def notifyChangeListeners(): Unit = {
    changeListeners.foreach(_.worldStateUpdated())
  }
}

object WorldState {
  private val logger = Logger(classOf[WorldState])

  private val DIRS = List((1, 0), (-1, 0), (0, 1), (0, -1))
  private val DEFAULT_MOBS_TO_SPAWN = 10

  private val generator: Random = new Random()
  private val dropItemGenerator = DropItemGenerator
  private val combatResolver: CombatResolver = CombatResolver()

  /**
    * Creates [[WorldState]] with the underlying [[TerrainMap]] of the given size.
    * Randomly places the [[PlayerCharacter]] and some [[MobCharacter]].
    */
  def apply(width: Int, height: Int): WorldState =
    apply(width, height, DEFAULT_MOBS_TO_SPAWN)

  private[model] def apply(width: Int, height: Int, mobsToSpawn: Int): WorldState = {
    val terrainMap = new TerrainMap(width, height)
    val (characterX, characterY) = generatePassablePosition(terrainMap)
    val character = PlayerCharacter(characterX, characterY)
    val mobs = mutable.ListBuffer[MobCharacter]()
    Range(0, mobsToSpawn).foreach { _ =>
      val (mobX, mobY) = generatePassablePosition(terrainMap)
      mobs.append(MobCharacter(mobX, mobY))
    }
    new WorldState(terrainMap, character, mobs.toList)
  }

  private def generatePassablePosition(terrainMap: TerrainMap): (Int, Int) = {
    var posX: Int = -1
    var posY: Int = -1
    do {
      posX = generator.nextInt(terrainMap.width)
      posY = generator.nextInt(terrainMap.height)
    } while (!terrainMap.getEntityAt(posX, posY).isPassable)
    (posX, posY)
  }
}
