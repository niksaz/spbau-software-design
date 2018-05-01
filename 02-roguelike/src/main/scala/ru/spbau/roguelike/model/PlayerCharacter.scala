package ru.spbau.roguelike.model

class PlayerCharacter private (
  override val posX: Int,
  override val posY: Int,
  private val baseStats: CombatStats,
  private val currentHealth: Int,
  private val inventory: Inventory,
)extends MapCharacter {

  def getCurrentHealth: Int = currentHealth

  def getStats: CombatStats = baseStats + inventory.getStats

  def getItems: List[InventoryItem] = inventory.getItems

  def addItem(item: Item): Unit = {
    inventory.addItem(item)
  }

  def invertIsEquippedItemWithIndex(itemIndex: Int): Unit = {
    inventory.invertIsEquippedItem(itemIndex)
  }

  override def moveTo(newX: Int, newY: Int): PlayerCharacter =
    new PlayerCharacter(newX, newY, baseStats, currentHealth, inventory)
}

object PlayerCharacter {
  def apply(posX: Int, posY: Int): PlayerCharacter = {
    val baseStats = CombatStats(200, 1, 1)
    new PlayerCharacter(posX, posY, baseStats, baseStats.health, new Inventory())
  }
}
