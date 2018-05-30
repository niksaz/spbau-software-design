package ru.spbau.roguelike.model.combat

/** Represents a character that can enter a combat. */
trait CombatCharacter {
  /** Property that allows to get the current health. */
  val currentHealth: Int

  /** Returns [[CombatStats]] for the character in the current moment. */
  def getStats: CombatStats

  /** Returns the character with the health reduced. */
  def reduceHealth(byPoints: Int): CombatCharacter
}
