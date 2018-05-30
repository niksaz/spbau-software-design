package ru.spbau.roguelike.model.characters

import ru.spbau.roguelike.model.combat.{CombatCharacter, CombatStats}

/** Represents a monster on a map. Has [[CombatStats]] from the spawn that do not change. */
class MobCharacter private (
  override val posX: Int,
  override val posY: Int,
  override val currentHealth: Int,
  private val stats: CombatStats,
) extends PositionalCharacter with CombatCharacter {

  override def getStats: CombatStats = stats

  override def moveTo(newX: Int, newY: Int): MobCharacter =
    new MobCharacter(newX, newY, currentHealth, stats)

  override def reduceHealth(byPoints: Int): MobCharacter =
    new MobCharacter(posX, posY, Math.max(0, currentHealth - byPoints), stats)
}

object MobCharacter {
  val defaultStats: CombatStats = CombatStats(10, 0, 2)

  /** Creates a [[MobCharacter]] with [[defaultStats]]. */
  def apply(posX: Int, posY: Int): MobCharacter =
    new MobCharacter(posX, posY, defaultStats.health, defaultStats)
}
