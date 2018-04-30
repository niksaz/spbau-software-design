package ru.spbau.roguelike.view

import java.util.function.Consumer

import org.codetome.zircon.api.builder.{LayerBuilder, TerminalBuilder, TextCharacterBuilder}
import org.codetome.zircon.api.color.{ANSITextColor, TextColorFactory}
import org.codetome.zircon.api.component.builder.{HeaderBuilder, PanelBuilder}
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.input.Input
import org.codetome.zircon.api.resource.{CP437TilesetResource, ColorThemeResource}
import org.codetome.zircon.api.screen.Screen
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.api.{Position, Size}
import ru.spbau.roguelike.model.{Floor, Wall, WorldState, WorldStateChangeListener}

class GameScreen(private val width: Int, private val height: Int) extends WorldStateChangeListener {
  private val messageHeader =
    HeaderBuilder.newBuilder()
      .position(Position.of(0, height + 1))
      .text("The world was created!")
      .build()

  private val healthHeader =
    HeaderBuilder.newBuilder()
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
      .text("Attack: 110")
      .build()

  private val terminal: Terminal =
    TerminalBuilder.newBuilder
      .initialTerminalSize(Size.of(width, height + 4))
      .font(CP437TilesetResource.ROGUE_YUN_16X16.toFont())
      .title("Roguelike")
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

    mainScreen.addComponent(messageHeader)
    mainScreen.addComponent(healthHeader)
    mainScreen.addComponent(armorHeader)
    mainScreen.addComponent(attackHeader)
    mainScreen.addComponent(inventoryHeader)

    overlay =
      new LayerBuilder()
        .size(mainScreen.getBoundableSize)
        .filler(
          TextCharacterBuilder.EMPTY
            .withBackgroundColor(TextColorFactory.fromRGB(0, 0, 0, 50)))
        .build
    mainScreen.pushLayer(overlay)

    inventoryScreen = TerminalBuilder.createScreenFor(terminal)
    inventoryScreen.setCursorVisibility(false)

    val panel =
      PanelBuilder.newBuilder()
        .wrapWithBox()
        .title("Inventory")
        .wrapWithShadow()
        .size(Size.of(width - 2, height))
        .position(Position.OFFSET_1x1)
        .build()

    val sword =
      HeaderBuilder.newBuilder()
        .position(Position.OFFSET_1x1)
        .text("[hands] Wooden sword (Attack: +5)")
        .build()

    val armor0 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(sword))
        .text("Wooden armor (Armor: +1)")
        .build()

    val armor1 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(armor0))
        .text("[body] Wooden armor (Armor: +1)")
        .build()

    val armor2 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(armor1))
        .text("Wooden armor (Armor: +1)")
        .build()

    val armor3 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(armor2))
        .text("Wooden armor (Armor: +1)")
        .build()

    val armor4 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(armor3))
        .text("Wooden armor (Armor: +1)")
        .build()

    val armor5 =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, 1).relativeToBottomOf(armor4))
        .text("Wooden armor (Armor: +1)")
        .build()

    sword.applyColorTheme(ColorThemeResource.HEADACHE.getTheme)
    armor0.applyColorTheme(GameScreen.DEFAULT_THEME)
    armor1.applyColorTheme(GameScreen.DEFAULT_THEME)
    armor2.applyColorTheme(GameScreen.DEFAULT_THEME)
    armor3.applyColorTheme(GameScreen.DEFAULT_THEME)
    armor4.applyColorTheme(GameScreen.DEFAULT_THEME)
    armor5.applyColorTheme(GameScreen.DEFAULT_THEME)

    panel.addComponent(sword)
    panel.addComponent(armor0)
    panel.addComponent(armor1)
    panel.addComponent(armor2)
    panel.addComponent(armor3)
    panel.addComponent(armor4)
    panel.addComponent(armor5)
    inventoryScreen.addComponent(panel)
    inventoryScreen.addComponent(healthHeader)
    inventoryScreen.addComponent(armorHeader)
    inventoryScreen.addComponent(attackHeader)
    inventoryScreen.addComponent(inventoryHeader)
  }

  def addOnInputListener(inputConsumer: Consumer[Input]): Unit = {
    terminal.onInput(inputConsumer)
  }

  override def receiveWorldState(worldState: WorldState): Unit = {
    if (worldState.getIsInInventory) {
      if (!currentScreen.eq(inventoryScreen)) {
        currentScreen = inventoryScreen
        currentScreen.display()
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
      currentScreen.display()
    }
  }
}

object GameScreen {
  private val DEFAULT_THEME = ColorThemeResource.TECH_LIGHT.getTheme
}
