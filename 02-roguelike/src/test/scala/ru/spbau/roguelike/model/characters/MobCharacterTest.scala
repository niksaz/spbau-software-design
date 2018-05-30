package ru.spbau.roguelike.model.characters

import org.scalatest.FunSuite

class MobCharacterTest extends FunSuite {

  test("MobCharacter") {
    var mobCharacter = MobCharacter(18, 13)
    mobCharacter = mobCharacter.reduceHealth(MobCharacter.defaultStats.health + 5)
    assert(mobCharacter.currentHealth == 0)
  }
}
