package ru.spbau.roguelike

import org.codetome.zircon.api.builder.{LayerBuilder, TerminalBuilder, TextCharacterBuilder}
import org.codetome.zircon.api.color.TextColorFactory
import org.codetome.zircon.api.component.builder.{HeaderBuilder, PanelBuilder}
import org.codetome.zircon.api.input.InputType
import org.codetome.zircon.api.resource.{CP437TilesetResource, ColorThemeResource}
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.api.{Position, Size}

object GameRunner {
  private val DEFAULT_THEME = ColorThemeResource.TECH_LIGHT.getTheme

  def main(args: Array[String]): Unit = {
    val width = 48
    val height = 21
    val terminal: Terminal =
      TerminalBuilder.newBuilder
        .initialTerminalSize(Size.of(width, height + 4))
        .font(CP437TilesetResource.ROGUE_YUN_16X16.toFont())
        .title("Roguelike")
        .build()

    var isInInventory: Boolean = false

    val screen = TerminalBuilder.createScreenFor(terminal)
    screen.setCursorVisibility(false)

    val messageHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, height + 1))
        .text("The world was created!")
        .build()

    val healthHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, height + 2))
        .text("Health: 100/100")
        .build()

    val armorHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(20, height + 2))
        .text("Armor: 100")
        .build()

    val attackHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(34, height + 2))
        .text("Attack: 110")
        .build()

    val inventoryHeader =
      HeaderBuilder.newBuilder()
        .position(Position.of(0, height + 3))
        .text("[I]nventory")
        .build()

    screen.addComponent(messageHeader)

    screen.addComponent(healthHeader)
    screen.addComponent(armorHeader)
    screen.addComponent(attackHeader)
    screen.addComponent(inventoryHeader)

    var worldState = new WorldState(width, height)

    val overlay =
      new LayerBuilder()
        .size(screen.getBoundableSize)
        .filler(
          TextCharacterBuilder.EMPTY
            .withBackgroundColor(TextColorFactory.fromRGB(0, 0, 0, 50)))
        .build
    screen.pushLayer(overlay)
    worldState.updateOverlay(overlay)

    val inventoryScreen = TerminalBuilder.createScreenFor(terminal)
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
    armor0.applyColorTheme(DEFAULT_THEME)
    armor1.applyColorTheme(DEFAULT_THEME)
    armor2.applyColorTheme(DEFAULT_THEME)
    armor3.applyColorTheme(DEFAULT_THEME)
    armor4.applyColorTheme(DEFAULT_THEME)
    armor5.applyColorTheme(DEFAULT_THEME)

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

    terminal.onInput { input =>
      if (input.isKeyStroke) {
        val keyStroke = input.asKeyStroke()
        if (isInInventory) {
          keyStroke.getInputType match {
            case InputType.ArrowDown =>
              sword.applyColorTheme(DEFAULT_THEME)
              inventoryScreen.refresh()
            case InputType.Character => keyStroke.getCharacter.toLower match {
              case 'i' =>
                isInInventory = false
                screen.display()
              case _ =>
            }
            case _ =>
          }
        } else {
          keyStroke.getInputType match {
            case InputType.ArrowUp =>
              worldState.moveUp()
              worldState.updateOverlay(overlay)
              screen.refresh()
            case InputType.ArrowDown =>
              worldState.moveDown()
              worldState.updateOverlay(overlay)
              screen.refresh()
            case InputType.ArrowLeft =>
              worldState.moveLeft()
              worldState.updateOverlay(overlay)
              screen.refresh()
            case InputType.ArrowRight =>
              worldState.moveRight()
              worldState.updateOverlay(overlay)
              screen.refresh()
            case InputType.Character => keyStroke.getCharacter.toLower match {
              case 'r' => worldState =
                new WorldState(width, height)
                worldState.updateOverlay(overlay)
                screen.refresh()
              case 'i' =>
                isInInventory = true
                inventoryScreen.display()
              case _ =>
            }
            case _ =>
          }
        }
      }
    }

    screen.display()
  }
}