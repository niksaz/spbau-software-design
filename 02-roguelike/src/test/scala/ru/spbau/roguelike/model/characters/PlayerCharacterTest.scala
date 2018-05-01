package ru.spbau.roguelike.model.characters

import org.scalatest.FunSuite
import ru.spbau.roguelike.model.combat.CombatStats
import ru.spbau.roguelike.model.items.{HandsItemSlot, Item}

class PlayerCharacterTest extends FunSuite {

  test("PlayerCharacterStats") {
    var playerCharacter = PlayerCharacter(0, 0)
    assert(playerCharacter.getStats == PlayerCharacter.defaultStats)
    val item = Item("test", CombatStats(10, 1, 1), HandsItemSlot)
    playerCharacter = playerCharacter.addItem(item)
    playerCharacter.invertIsEquippedItemWithIndex(0)
    assert(playerCharacter.getStats == PlayerCharacter.defaultStats + item.stats)
  }
}
