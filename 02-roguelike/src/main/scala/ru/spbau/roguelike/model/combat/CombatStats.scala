package ru.spbau.roguelike.model.combat

case class CombatStats(
  var health: Int,
  var armor: Int,
  var attack: Int,
) {
  def +(that: CombatStats) =
    CombatStats(health + that.health, armor + that.armor, attack + that.attack)
}
