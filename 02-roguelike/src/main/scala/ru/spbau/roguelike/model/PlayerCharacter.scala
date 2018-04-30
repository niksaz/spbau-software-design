package ru.spbau.roguelike.model

class PlayerCharacter {
  private val baseStats = GameStats(100, 10, 2)
  private var curHealth: Int = baseStats.health
  private var inventory: Inventory = new Inventory()

  def getStats: GameStats = baseStats + inventory.getStats
}
