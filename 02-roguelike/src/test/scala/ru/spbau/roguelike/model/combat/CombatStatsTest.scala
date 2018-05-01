package ru.spbau.roguelike.model.combat

import org.scalatest.FunSuite

class CombatStatsTest extends FunSuite {

  test("CombatStatsSum") {
    val combatStats1 = CombatStats(10, 15, 1)
    val combatStats2 = CombatStats(100, 0, 20)
    val sumCombatStats = combatStats1 + combatStats2
    val expectedCombatStats =
      CombatStats(
        combatStats1.health + combatStats2.health,
        combatStats1.armor + combatStats2.armor,
        combatStats1.attack + combatStats2.attack)
    assert(sumCombatStats == expectedCombatStats)
  }
}
