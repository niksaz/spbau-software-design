package ru.spbau.roguelike.model.combat

/** An entity that can resolve a combat between two [[CombatCharacter]]s. */
trait CombatResolver {
  /** Conducts a fight and returns [[CombatCharacter]]s after the combat. */
  def resolveCombat(
    firstCharacter: CombatCharacter, secondCharacter: CombatCharacter
  ): (CombatCharacter, CombatCharacter)
}

object CombatResolver {
  /** Returns [[OneRoundCombatResolver]] implementation of [[CombatResolver]]. */
  def apply(): CombatResolver = OneRoundCombatResolver
}
