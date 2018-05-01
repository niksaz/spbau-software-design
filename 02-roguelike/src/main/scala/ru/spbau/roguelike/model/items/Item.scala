package ru.spbau.roguelike.model.items

import ru.spbau.roguelike.model.combat.CombatStats

sealed trait CharacterItemSlot {
  val name: String
}

case object HandsItemSlot extends CharacterItemSlot {
  override val name: String = "Hands"
}

case object BodyItemSlot extends CharacterItemSlot {
  override val name: String = "Body"
}

case class Item(name: String, stats: CombatStats, itemSlot: CharacterItemSlot)
