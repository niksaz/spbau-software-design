package ru.spbau.roguelike.model

class PlayerCharacter private (
  override val posX: Int,
  override val posY: Int,
  private val stats: CombatStats,
  private val currentHealth: Int,
  private val inventory: Inventory,
)extends PositionalCharacter {

  def getCurrentHealth: Int = currentHealth

  def getStats: CombatStats = stats + inventory.getStats

  def getItems: List[InventoryItem] = inventory.getItems

  def addItem(item: Item): Unit = {
    inventory.addItem(item)
  }

  def invertIsEquippedItemWithIndex(itemIndex: Int): Unit = {
    inventory.invertIsEquippedItem(itemIndex)
  }

  override def moveTo(newX: Int, newY: Int): PlayerCharacter =
    new PlayerCharacter(newX, newY, stats, currentHealth, inventory)
}

object PlayerCharacter {
  def apply(posX: Int, posY: Int): PlayerCharacter = {
    val stats = CombatStats(200, 1, 1)
    new PlayerCharacter(posX, posY, stats, stats.health, new Inventory())
  }
}
