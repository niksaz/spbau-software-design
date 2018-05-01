package ru.spbau.roguelike.model.character

import ru.spbau.roguelike.model.combat.CombatStats

case class InventoryItem(item: Item, var isEquipped: Boolean)

class Inventory {
  private var items: List[InventoryItem] = List()

  def getItems: List[InventoryItem] = items

  def getStats: CombatStats =
    items.filter(_.isEquipped).foldLeft(CombatStats(0, 0, 0))(_ + _.item.stats)

  def addItem(item: Item): Unit = {
    items = InventoryItem(item, isEquipped = false) :: items
  }

  def invertIsEquippedItem(itemIndex: Int): Unit = {
    val itemToInvert = items(itemIndex)
    if (!itemToInvert.isEquipped) {
      items.filter(_.item.itemSlot == itemToInvert.item.itemSlot).foreach { item =>
        item.isEquipped = false
      }
    }
    items(itemIndex).isEquipped ^= true
  }
}
