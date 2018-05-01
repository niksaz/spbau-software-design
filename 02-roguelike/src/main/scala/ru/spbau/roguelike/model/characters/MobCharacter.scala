package ru.spbau.roguelike.model.characters

import ru.spbau.roguelike.model.combat.{CombatCharacter, CombatStats}

class MobCharacter(
  override val posX: Int,
  override val posY: Int,
  override val currentHealth: Int,
  private val stats: CombatStats,
) extends PositionalCharacter with CombatCharacter {

  override def getStats: CombatStats = stats

  override def moveTo(newX: Int, newY: Int): MobCharacter =
    new MobCharacter(newX, newY, currentHealth, stats)

  override def reduceHealth(byPoints: Int): CombatCharacter =
    new MobCharacter(posX, posY, Math.max(0, currentHealth - byPoints), stats)
}

object MobCharacter {
  def apply(posX: Int, posY: Int): MobCharacter = {
    val stats = CombatStats(10, 0, 2)
    new MobCharacter(posX, posY, stats.health, stats)
  }
}
