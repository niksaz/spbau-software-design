package ru.spbau.roguelike.model.characters

import ru.spbau.roguelike.model.combat.{CombatCharacter, CombatStats}
import ru.spbau.roguelike.model.items.Item

/** Represents the player's character on a map. Has an [[Inventory]]. */
class PlayerCharacter private (
  override val posX: Int,
  override val posY: Int,
  override val currentHealth: Int,
  private val stats: CombatStats,
  private val inventory: Inventory,
) extends PositionalCharacter with CombatCharacter {

  override def toString = s"PlayerCharacter($posX, $posY, $currentHealth, $stats, $getItems)"

  def getCurrentHealth: Int = currentHealth

  /** Returns the player's [[InventoryItem]]s. */
  def getItems: List[InventoryItem] = inventory.items

  /** Returns a character with the item added to the character's inventory. */
  def addItem(item: Item): PlayerCharacter = {
    val newInventory = inventory.addItem(item)
    new PlayerCharacter(posX, posY, currentHealth, stats, newInventory)
  }

  /** Inverts the state of index-th item in the character's inventory. */
  def invertIsEquippedItemWithIndex(itemIndex: Int): Unit = {
    inventory.invertIsEquippedItem(itemIndex)
  }

  /** Returns the character's [[CombatStats]] which are combined from base and items' stats. */
  override def getStats: CombatStats = stats + inventory.getStats

  override def reduceHealth(byPoints: Int): CombatCharacter =
    new PlayerCharacter(posX, posY, Math.max(0, currentHealth - byPoints), stats, inventory)

  override def moveTo(newX: Int, newY: Int): PlayerCharacter =
    new PlayerCharacter(newX, newY, currentHealth, stats, inventory)
}

object PlayerCharacter {
  val defaultStats: CombatStats = CombatStats(100, 0, 1)

  /** Creates a [[PlayerCharacter]] with [[defaultStats]]. */
  def apply(posX: Int, posY: Int): PlayerCharacter =
    new PlayerCharacter(posX, posY, defaultStats.health, defaultStats, Inventory())
}
