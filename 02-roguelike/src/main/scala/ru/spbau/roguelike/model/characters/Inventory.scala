package ru.spbau.roguelike.model.characters

import ru.spbau.roguelike.model.combat.CombatStats
import ru.spbau.roguelike.model.items.Item

/** Stores the [[Item]] and its state in the inventory. */
case class InventoryItem(item: Item, var isEquipped: Boolean)

/** Represents items which were picked up by [[PlayerCharacter]]. */
class Inventory private(val items: List[InventoryItem]) {
  /** Returns the [[CombatStats]] for all equipped items. */
  def getStats: CombatStats =
    items.filter(_.isEquipped).foldLeft(CombatStats(0, 0, 0))(_ + _.item.stats)

  /** Adds an [[Item]] to the [[Inventory]]. Returns an [[Inventory]] with the given item added. */
  def addItem(item: Item): Inventory =
    new Inventory(InventoryItem(item, isEquipped = false) :: items)

  /** Changes the equipped status of the item to the opposite. */
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
  /** Create an empty [[Inventory]]. */
  def apply(): Inventory = new Inventory(List())
}
