package ru.spbau.roguelike.model.character

import ru.spbau.roguelike.model.combat.{CombatCharacter, CombatStats}

class PlayerCharacter private (
  override val posX: Int,
  override val posY: Int,
  override val currentHealth: Int,
  private val stats: CombatStats,
  private val inventory: Inventory,
) extends PositionalCharacter with CombatCharacter {

  def getCurrentHealth: Int = currentHealth

  def getStats: CombatStats = stats + inventory.getStats

  def getItems: List[InventoryItem] = inventory.getItems

  def addItem(item: Item): Unit = {
    inventory.addItem(item)
  }

  def invertIsEquippedItemWithIndex(itemIndex: Int): Unit = {
    inventory.invertIsEquippedItem(itemIndex)
  }

  override def reduceHealth(byPoints: Int): CombatCharacter =
    new PlayerCharacter(posX, posY, Math.max(0, currentHealth - byPoints), stats, inventory)

  override def moveTo(newX: Int, newY: Int): PlayerCharacter =
    new PlayerCharacter(newX, newY, currentHealth, stats, inventory)
}

object PlayerCharacter {
  def apply(posX: Int, posY: Int): PlayerCharacter = {
    val stats = CombatStats(100, 1, 1)
    new PlayerCharacter(posX, posY, stats.health, stats, new Inventory())
  }
}
