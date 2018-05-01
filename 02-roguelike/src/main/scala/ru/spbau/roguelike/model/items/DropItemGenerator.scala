package ru.spbau.roguelike.model.items

import ru.spbau.roguelike.model.characters.{MobCharacter, PlayerCharacter}
import ru.spbau.roguelike.model.combat.CombatStats

import scala.util.Random

/** Represents an [[Item]] with a relative chance to be dropped. */
private case class ItemDrop(item: Item, dropChanceContribution: Int)

/** Object which can used to generate random items on the map. */
object DropItemGenerator {
  private val generator = new Random()

  private[items] val drops = List(
    ItemDrop(Item("Wooden armor", CombatStats(0, 1, 0), BodyItemSlot), 100),
    ItemDrop(Item("Wooden sword", CombatStats(0, 0, 1), HandsItemSlot), 100),
    ItemDrop(Item("Silver armor", CombatStats(0, 2, 0), BodyItemSlot), 25),
    ItemDrop(Item("Silver sword", CombatStats(0, 0, 4), HandsItemSlot), 25),
    ItemDrop(Item("Golden armor", CombatStats(0, 3, 0), BodyItemSlot), 5),
    ItemDrop(Item("Golden sword", CombatStats(0, 0, 9), HandsItemSlot), 5))

  /**
    * Returns an [[Item]] or nothing depending on the given [[PlayerCharacter]] and the defeated
    * [[MobCharacter]].
    */
  def generateDropItem(character: PlayerCharacter, mobCharacter: MobCharacter): Option[Item] = {
    val dropContributionTotal = drops.map(_.dropChanceContribution).sum
    val ContributionIndex = generator.nextInt(dropContributionTotal)
    findSelectedItem(ContributionIndex, 0)
  }

  private[items] def findSelectedItem(contributionIndex: Int, dropIndex: Int): Option[Item] =
    if (dropIndex == drops.size) {
      Option.empty
    } else {
      val drop = drops(dropIndex)
      if (contributionIndex < drop.dropChanceContribution) {
        Option(drop.item)
      } else {
        findSelectedItem(contributionIndex - drop.dropChanceContribution, dropIndex + 1)
      }
    }
}
