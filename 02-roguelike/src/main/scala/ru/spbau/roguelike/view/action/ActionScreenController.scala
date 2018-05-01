package ru.spbau.roguelike.view.action

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.component.builder.{HeaderBuilder, LabelBuilder}
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.model.character.PlayerCharacter
import ru.spbau.roguelike.model.terrain.{Floor, Wall}
import ru.spbau.roguelike.view.AbstractScreenController

class ActionScreenController(
  worldState: WorldState,
  terminal: Terminal
) extends AbstractScreenController(worldState, terminal) {

  private val healthHeader =
    LabelBuilder.newBuilder()
      .position(Position.of(0, height - 2))
      .text("Health:")
      .build()

  private val armorHeader =
    HeaderBuilder.newBuilder()
      .position(Position.of(width / 3 + 4, height - 2))
      .text("Armor:")
      .build()

  private val attackHeader =
    HeaderBuilder.newBuilder()
      .position(Position.of(width / 3 * 2 + 4, height - 2))
      .text("Attack:")
      .build()

  {
    screen.setCursorVisibility(false)

    val inventoryHeader =
      HeaderBuilder.newBuilder()
        .position(
          Position.of(armorHeader.getPosition.getColumn, armorHeader.getPosition.getRow + 1))
        .text("[I]nventory")
        .build()

    val quitHeader =
      HeaderBuilder.newBuilder()
        .position(
          Position.of(attackHeader.getPosition.getColumn, attackHeader.getPosition.getRow + 1))
        .text("[Q]uit")
        .build()

    screen.addComponent(healthHeader)
    screen.addComponent(armorHeader)
    screen.addComponent(attackHeader)
    screen.addComponent(inventoryHeader)
    screen.addComponent(quitHeader)
    screen.pushLayer(overlay)
  }

  override def redraw(): Unit = {
    clearOverlay()
    val terrainMap = worldState.getTerrainMap
    for (row <- 0 until terrainMap.height) {
      for (column <- 0 until terrainMap.width) {
        val charToSet = terrainMap.getEntityAt(column, row) match {
          case Wall() => '#'
          case Floor() => '.'
        }
        overlay.setCharacterAt(Position.of(column, row), charToSet)
        overlay.resetColorsAndModifiers()
      }
    }
    worldState.getMobs.foreach { mob =>
      overlay.setBackgroundColor(ANSITextColor.RED)
      overlay.setCharacterAt(Position.of(mob.posX, mob.posY), 'x')
    }
    val character = worldState.getCharacter
    overlay.setBackgroundColor(ANSITextColor.GREEN)
    overlay.setCharacterAt(Position.of(character.posX, character.posY), '*')
    overlay.resetColorsAndModifiers()
    overlay.putText("The world was created!", Position.of(0, height - 3))
    putStatsOnOverlay(worldState.getCharacter)
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
