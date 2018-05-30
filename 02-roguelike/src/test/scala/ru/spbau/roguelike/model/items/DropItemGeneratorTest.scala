package ru.spbau.roguelike.model.items

import org.scalatest.FunSuite

class DropItemGeneratorTest extends FunSuite {

  test("findSelectedItem") {
    val itemDropped =
      DropItemGenerator.findSelectedItem(
        DropItemGenerator.drops.head.dropChanceContribution +
          DropItemGenerator.drops(1).dropChanceContribution,
        0)
    assert(itemDropped.nonEmpty)
    assert(itemDropped.get == DropItemGenerator.drops(2).item)
  }
}
