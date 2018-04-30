package ru.spbau.roguelike.view

import java.util.function.Consumer

import org.codetome.zircon.api.builder.{LayerBuilder, TerminalBuilder, TextCharacterBuilder}
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.component.builder.{HeaderBuilder, LabelBuilder, PanelBuilder}
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.resource.{CP437TilesetResource, ColorThemeResource}
import org.codetome.zircon.api.screen.Screen
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.api.{Position, Size}
import ru.spbau.roguelike.model._

import scala.collection.mutable

class GameScreen(private val width: Int, private val height: Int) extends WorldStateChangeListener {
  private val terminal: Terminal =
    TerminalBuilder.newBuilder
      .initialTerminalSize(Size.of(width, height + 4))
      .font(CP437TilesetResource.ROGUE_YUN_16X16.toFont())
      .title("Roguelike")
      .build()

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
      .position(Position.of(34, height + 2))
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

  private var mainScreen: Screen = _
  private var inventoryScreen: Screen = _
  private var currentScreen: Screen = _

  private var overlay: Layer = _

  {
    mainScreen = TerminalBuilder.createScreenFor(terminal)
    mainScreen.setCursorVisibility(false)

    val inventoryHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, height + 3))
        .text("[I]nventory")
        .build()

    mainScreen.addComponent(healthHeader)
    mainScreen.addComponent(armorHeader)
    mainScreen.addComponent(attackHeader)
    mainScreen.addComponent(inventoryHeader)

    overlay =
      new LayerBuilder()
        .size(mainScreen.getBoundableSize)
        .filler(TextCharacterBuilder.EMPTY)
        .build
    mainScreen.pushLayer(overlay)

    inventoryScreen = TerminalBuilder.createScreenFor(terminal)
    inventoryScreen.setCursorVisibility(false)

    inventoryScreen.addComponent(panel)
    inventoryScreen.addComponent(healthHeader)
    inventoryScreen.addComponent(armorHeader)
    inventoryScreen.addComponent(attackHeader)
    inventoryScreen.addComponent(inventoryHeader)

    inventoryScreen.pushLayer(overlay)
  }

  def addOnInputListener(inputConsumer: Consumer[Input]): Unit = {
    terminal.onInput(inputConsumer)
  }

  override def receiveWorldState(worldState: WorldState): Unit = {
    clearOverlay()
    val character = worldState.getCharacter
    if (worldState.getIsInInventory) {
      if (!currentScreen.eq(inventoryScreen)) {
        currentScreen = inventoryScreen
        currentScreen.display()
      }
      val column = panel.getPosition.getColumn + 2
      var row = panel.getPosition.getRow + 2
      val items = character.getItems
      items.indices.foreach { index =>
        val item = items(index)
        val itemLabel = f"${item.name} ${GameScreen.statsToString(item.stats)} @ ${item.bodyPart}"
        if (index == worldState.getInventoryPosition) {
          overlay.setBackgroundColor(ANSITextColor.GREEN)
        }
        if (row < panel.getPosition.getRow + panel.getEffectiveSize.getRows) {
          overlay.putText(itemLabel, Position.of(column, row))
        }
        overlay.resetColorsAndModifiers()
        row += 2
      }
    } else {
      if (!currentScreen.eq(mainScreen)) {
        currentScreen = mainScreen
        currentScreen.display()
      }
      val worldMap = worldState.getWorldMap
      for (row <- 0 until height) {
        for (column <- 0 until width) {
          val charToSet = worldMap.getEntityAt(column, row) match {
            case Wall() => '#'
            case Floor() => '.'
          }
          overlay.setCharacterAt(Position.of(column, row), charToSet)
          overlay.resetColorsAndModifiers()
        }
      }
      overlay.setBackgroundColor(ANSITextColor.GREEN)
      overlay.setCharacterAt(Position.of(worldState.getCharacterX, worldState.getCharacterY), '*')
      overlay.resetColorsAndModifiers()
    }
    overlay.putText("The world was created!", Position.of(0, height + 1))
    putStatsOnOverlay(character)
    currentScreen.refresh()
  }

  private def clearOverlay(): Unit = {
    for (column <- 0 until width) {
      for (row <- 0 until (height + 4)) {
        overlay.setCharacterAt(Position.of(column, row), TextCharacterBuilder.EMPTY)
      }
    }
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

object GameScreen {
  private val DEFAULT_THEME = ColorThemeResource.TECH_LIGHT.getTheme

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
