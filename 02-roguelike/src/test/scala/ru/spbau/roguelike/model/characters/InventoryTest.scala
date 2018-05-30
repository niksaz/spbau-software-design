package ru.spbau.roguelike.model.characters

import org.scalatest.FunSuite
import ru.spbau.roguelike.model.combat.CombatStats
import ru.spbau.roguelike.model.items.{BodyItemSlot, HandsItemSlot, Item}

class InventoryTest extends FunSuite {

  test("Inventory") {
    var inventory = Inventory()
    val item2 = Item("test", CombatStats(10, 1, 1), HandsItemSlot)
    inventory = inventory.addItem(item2)
    assert(inventory.getStats == CombatStats(0, 0, 0))
    inventory.invertIsEquippedItem(0)
    assert(inventory.getStats == item2.stats)
    val item1 = Item("test2", CombatStats(5, 0, 3), BodyItemSlot)
    inventory = inventory.addItem(item1)
    inventory.invertIsEquippedItem(0)
    assert(inventory.getStats == item1.stats + item2.stats)
  }

  test("InventorySameSlot") {
    var inventory = Inventory()
    val item2 = Item("test", CombatStats(10, 1, 1), BodyItemSlot)
    val item1 = Item("test2", CombatStats(5, 0, 3), BodyItemSlot)
    inventory = inventory.addItem(item2)
    inventory = inventory.addItem(item1)
    inventory.invertIsEquippedItem(0)
    assert(inventory.getStats == item1.stats)
    inventory.invertIsEquippedItem(1)
    assert(inventory.getStats == item2.stats)
  }
}
