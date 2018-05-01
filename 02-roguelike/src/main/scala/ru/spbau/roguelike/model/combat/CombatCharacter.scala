package ru.spbau.roguelike.model.combat

trait CombatCharacter {
  val currentHealth: Int

  def getStats: CombatStats

  def reduceHealth(byPoints: Int): CombatCharacter
}
