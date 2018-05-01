package ru.spbau.roguelike.model

class MobCharacter(
  override val posX: Int,
  override val posY: Int,
  private val stats: CombatStats,
  private val currentHealth: Int,
) extends PositionalCharacter {

  override def moveTo(newX: Int, newY: Int): MobCharacter =
    new MobCharacter(newX, newY, stats, currentHealth)
}


object MobCharacter {
  def apply(posX: Int, posY: Int): MobCharacter = {
    val stats = CombatStats(10, 1, 2)
    new MobCharacter(posX, posY, stats, stats.health)
  }
}
