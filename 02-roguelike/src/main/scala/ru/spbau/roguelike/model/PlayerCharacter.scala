package ru.spbau.roguelike.model

class PlayerCharacter {
  private val baseStats = CombatStats(200, 1, 1)
  private var currentHealth: Int = baseStats.health
  private val inventory: Inventory = new Inventory()

  def getCurrentHealth: Int = currentHealth

  def getStats: CombatStats = baseStats + inventory.getStats

  def getItems: List[InventoryItem] = inventory.getItems

  def addItem(item: Item): Unit = {
    inventory.addItem(item)
  }

  def invertIsEquippedItemWithIndex(itemIndex: Int): Unit = {
    inventory.invertIsEquippedItem(itemIndex)
  }
}
