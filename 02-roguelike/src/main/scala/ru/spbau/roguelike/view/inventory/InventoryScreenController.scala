package ru.spbau.roguelike.view.inventory

import org.codetome.zircon.api.builder.{LayerBuilder, TextCharacterBuilder}
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.component.builder.{HeaderBuilder, LabelBuilder, PanelBuilder}
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.api.{Position, Size}
import ru.spbau.roguelike.model.{CombatStats, InventoryItem, PlayerCharacter, WorldState}
import ru.spbau.roguelike.view.AbstractScreenController

import scala.collection.mutable

class InventoryScreenController(
  worldState: WorldState,
  terminal: Terminal
) extends AbstractScreenController(worldState, terminal) {

  private val height = worldState.getWorldMap.height
  private val width = worldState.getWorldMap.width

  private val overlay: Layer =
    new LayerBuilder()
      .size(screen.getBoundableSize)
      .filler(TextCharacterBuilder.EMPTY)
      .build

  private val healthHeader =
    LabelBuilder.newBuilder()
      .position(Position.of(0, height + 2))
      .text("Health:")
      .build()

  private val armorHeader =
    HeaderBuilder.newBuilder()
      .position(Position.of(width / 3 + 4, height + 2))
      .text("Armor:")
      .build()

  private val attackHeader =
    HeaderBuilder.newBuilder()
      .position(Position.of(width / 3 * 2 + 4, height + 2))
      .text("Attack:")
      .build()

  private val panel =
    PanelBuilder.newBuilder()
      .wrapWithBox()
      .title("Inventory")
      .wrapWithShadow()
      .size(Size.of(width - 2, height))
      .position(Position.OFFSET_1x1)
      .build()

  private val maxItemsToShow: Int = panel.getEffectiveSize.getRows / 2
  private var inventoryPosition: Int = 0
  private var indexesRange = Range(0, 0)

  {
    screen.setCursorVisibility(false)

    screen.addComponent(panel)
    screen.addComponent(healthHeader)
    screen.addComponent(armorHeader)
    screen.addComponent(attackHeader)

    val inventoryHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, height + 3))
        .text("[I]nventory")
        .build()

    screen.addComponent(inventoryHeader)

    screen.pushLayer(overlay)
  }

  def getInventoryPosition: Int = inventoryPosition

  def moveUpInInventory(): Unit = {
    inventoryPosition -= 1
    if (inventoryPosition < 0) {
      inventoryPosition += worldState.getCharacter.getItems.size
    }
    redraw()
  }

  def moveDownInInventory(): Unit = {
    inventoryPosition += 1
    if (inventoryPosition == worldState.getCharacter.getItems.size) {
      inventoryPosition = 0
    }
    redraw()
  }

  override def redraw(): Unit = {
    clearOverlay()
    val character = worldState.getCharacter
    drawItems(character.getItems, inventoryPosition)
    putStatsOnOverlay(character)
    screen.display()
  }

  private def clearOverlay(): Unit = {
    val size = overlay.getBoundableSize
    for (column <- 0 until size.getColumns) {
      for (row <- 0 until size.getRows) {
        overlay.setCharacterAt(Position.of(column, row), TextCharacterBuilder.EMPTY)
      }
    }
  }


  private def drawItems(items: List[InventoryItem], selectedItemIndex: Int): Unit = {
    normalizeIndexesRange(items, selectedItemIndex)
    val column = panel.getPosition.getColumn + 2
    var row = panel.getPosition.getRow + 2
    items.indices.slice(indexesRange.start, indexesRange.end).foreach { index =>
      if (index == selectedItemIndex) {
        overlay.setBackgroundColor(ANSITextColor.GREEN)
      }
      val inventoryItem = items(index)
      overlay.putText(
        InventoryScreenController.inventoryItemToString(inventoryItem), Position.of(column, row))
      overlay.resetColorsAndModifiers()
      row += 2
    }
  }

  private def normalizeIndexesRange(items: List[InventoryItem], selectedItemIndex: Int): Unit = {
    val itemsSize = items.size
    var start = indexesRange.start
    var end = indexesRange.end
    while (start >= itemsSize) {
      start -= 1
    }
    while (end >= itemsSize) {
      end -= 1
    }
    while (end - start < maxItemsToShow && end != itemsSize) {
      end += 1
    }
    while (end - start < maxItemsToShow && start != 0) {
      start -= 1
    }
    while (selectedItemIndex >= end) {
      start += 1
      end += 1
    }
    while (selectedItemIndex < start) {
      start -= 1
      end -= 1
    }
    indexesRange = Range(start, end)
  }

  private def putStatsOnOverlay(character: PlayerCharacter): Unit = {
    val charStats = character.getStats
    val currentHealth = character.getCurrentHealth

    val row = healthHeader.getPosition.getRow

    val healthColumn = healthHeader.getPosition.component1() + healthHeader.getText.length + 1
    val healthLabel = f"$currentHealth/${charStats.health}"
    overlay.putText(healthLabel, Position.of(healthColumn, row))

    val armorColumn = armorHeader.getPosition.component1() + armorHeader.getText.length + 1
    val armorLabel = f"${charStats.armor}"
    overlay.putText(armorLabel, Position.of(armorColumn, row))

    val attackColumn = attackHeader.getPosition.component1() + attackHeader.getText.length + 1
    val attackLabel = f"${charStats.attack}"
    overlay.putText(attackLabel, Position.of(attackColumn, row))
  }
}

object InventoryScreenController {
  private def inventoryItemToString(inventoryItem: InventoryItem): String = {
    val item = inventoryItem.item
    val inventoryItemDesc = new mutable.StringBuilder()
    if (inventoryItem.isEquipped) {
      inventoryItemDesc.append("[*] ")
    }
    val itemLabel = f"${item.name} ${statsToString(item.stats)} @ ${item.bodyPart}"
    inventoryItemDesc.append(itemLabel)
    inventoryItemDesc.toString()
  }

  private def statsToString(stats: CombatStats): String = {
    val nonzeroStats = mutable.ListBuffer[String]()
    if (stats.health != 0) {
      nonzeroStats.append(f"HP: +${stats.health}")
    }
    if (stats.armor != 0) {
      nonzeroStats.append(f"ARM: +${stats.armor}")
    }
    if (stats.attack != 0) {
      nonzeroStats.append(f"ATT: +${stats.attack}")
    }
    nonzeroStats.mkString("(", ", ", ")")
  }
}
