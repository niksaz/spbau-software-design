package ru.spbau.roguelike.model.combat

object CombatResolverImpl extends CombatResolver {
  override def resolveFight(
    firstCharacter: CombatCharacter, secondCharacter: CombatCharacter
  ): (CombatCharacter, CombatCharacter) = {
    val firstCharHealthReduced =
      Math.max(0, secondCharacter.getStats.attack - firstCharacter.getStats.armor)
    val secondCharHealthReduced =
      Math.max(0, firstCharacter.getStats.attack - secondCharacter.getStats.armor)
    val afterfightFirstCharacter = firstCharacter.reduceHealth(firstCharHealthReduced)
    val afterfightSecondCharacter = secondCharacter.reduceHealth(secondCharHealthReduced)
    (afterfightFirstCharacter, afterfightSecondCharacter)
  }
}
