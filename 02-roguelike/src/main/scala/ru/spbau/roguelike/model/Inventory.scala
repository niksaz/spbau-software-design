package ru.spbau.roguelike.model

class Inventory {
  private val equippedItems: List[Item] = List()
  private val storedItems: List[Item] = List()

  def getStats: GameStats = equippedItems.foldLeft(GameStats(0, 0, 0))(_ + _.stats)
}
