package ru.spbau.roguelike.model.combat

import org.scalatest.FunSuite
import ru.spbau.roguelike.model.characters.{MobCharacter, PlayerCharacter}

class OneRoundCombatResolverTest extends FunSuite {

  test("resolveFight") {
    val playerCharacter = PlayerCharacter(0, 0)
    val mobCharacter = MobCharacter(0, 0)
    val afterFightChars = OneRoundCombatResolver.resolveCombat(playerCharacter, mobCharacter)
    val afterFightPlayerCharacter = afterFightChars._1
    val afterFightMobCharacter = afterFightChars._2

    val expectedPlayerCharacterHP =
      PlayerCharacter.defaultStats.health -
        Math.max(0, MobCharacter.defaultStats.attack - PlayerCharacter.defaultStats.armor)
    assert(afterFightPlayerCharacter.currentHealth == expectedPlayerCharacterHP)

    val expectedMobCharacterHP =
      MobCharacter.defaultStats.health -
        Math.max(0, PlayerCharacter.defaultStats.attack - MobCharacter.defaultStats.armor)
    assert(afterFightMobCharacter.currentHealth == expectedMobCharacterHP)
  }
}
