package ru.spbau.roguelike.model.combat

import com.typesafe.scalalogging.Logger

/** One-round [[CombatResolver]]. */
object OneRoundCombatResolver extends CombatResolver {
  private val logger = Logger("ru.spbau.roguelike.model.combat.CombatResolverImpl")

  /**
    * Deals the damage done in one round by [[CombatCharacter]] based on their [[CombatStats]].
    * Returns [[CombatCharacter]]s after the combat.
    */
  override def resolveCombat(
    firstCharacter: CombatCharacter, secondCharacter: CombatCharacter
  ): (CombatCharacter, CombatCharacter) = {
    val firstCharHealthReduced =
      Math.max(0, secondCharacter.getStats.attack - firstCharacter.getStats.armor)
    val secondCharHealthReduced =
      Math.max(0, firstCharacter.getStats.attack - secondCharacter.getStats.armor)
    logger.info(s"Reducing health of 1-st Char by $firstCharHealthReduced")
    val afterfightFirstCharacter = firstCharacter.reduceHealth(firstCharHealthReduced)
    logger.info(s"Reducing health of 2-nd Char by $secondCharHealthReduced")
    val afterfightSecondCharacter = secondCharacter.reduceHealth(secondCharHealthReduced)
    (afterfightFirstCharacter, afterfightSecondCharacter)
  }
}