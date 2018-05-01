package ru.spbau.roguelike.model.combat

/** Encapsulates the statistics which are important for a combat. */
case class CombatStats(
  var health: Int,
  var armor: Int,
  var attack: Int,
) {
  /** Returns a component-wise sum with the other [[CombatStats]]. */
  def +(other: CombatStats): CombatStats =
    CombatStats(health + other.health, armor + other.armor, attack + other.attack)
}
