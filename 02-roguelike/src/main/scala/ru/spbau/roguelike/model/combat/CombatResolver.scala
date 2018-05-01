package ru.spbau.roguelike.model.combat

trait CombatResolver {
  def resolveFight(
    firstCharacter: CombatCharacter, secondCharacter: CombatCharacter
  ): (CombatCharacter, CombatCharacter)
}

object CombatResolver {
  def apply(): CombatResolver = CombatResolverImpl
}
