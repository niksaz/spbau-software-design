package ru.spbau.roguelike.model

class Inventory {
  private var equippedItems = List[Item]()
  private var storedItems = List[Item]()

  def getStats: CombatStats = equippedItems.foldLeft(CombatStats(0, 0, 0))(_ + _.stats)

  def addItem(item: Item): Unit = {
    storedItems = item :: storedItems
  }

  def getItems: List[Item] = equippedItems ++ storedItems
}
