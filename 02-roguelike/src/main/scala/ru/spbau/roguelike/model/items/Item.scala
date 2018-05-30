package ru.spbau.roguelike.model.items

import ru.spbau.roguelike.model.combat.CombatStats

/** Trait which stands for a slot of [[ru.spbau.roguelike.model.characters.PlayerCharacter]]. */
sealed trait CharacterItemSlot {
  /** Returns the displayed name of the slot. */
  val name: String
}

/** A slot for character's hands. */
case object HandsItemSlot extends CharacterItemSlot {
  override val name: String = "Hands"
}

/** A slot for character's body. */
case object BodyItemSlot extends CharacterItemSlot {
  override val name: String = "Body"
}

/** Represents an item what can be found on the map by a player. */
case class Item(name: String, stats: CombatStats, itemSlot: CharacterItemSlot)
