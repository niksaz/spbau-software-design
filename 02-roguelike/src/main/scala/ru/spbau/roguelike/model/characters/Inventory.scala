package ru.spbau.roguelike.model.characters

import ru.spbau.roguelike.model.combat.CombatStats
import ru.spbau.roguelike.model.items.Item

case class InventoryItem(item: Item, var isEquipped: Boolean)

class Inventory private (
  private val items: List[InventoryItem]
) {
  def getItems: List[InventoryItem] = items

  def getStats: CombatStats =
    items.filter(_.isEquipped).foldLeft(CombatStats(0, 0, 0))(_ + _.item.stats)

  def addItem(item: Item): Inventory =
     new Inventory(InventoryItem(item, isEquipped = false) :: items)

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

object Inventory {
  def apply(): Inventory = new Inventory(List())
}