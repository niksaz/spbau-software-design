package ru.spbau.roguelike.view.map

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.component.builder.{HeaderBuilder, LabelBuilder}
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.model.characters.PlayerCharacter
import ru.spbau.roguelike.model.terrain.{FloorMapEntity, WallMapEntity}
import ru.spbau.roguelike.view.AbstractScreenController

/** The controller of the screen that is shown when the player walks around the map. */
class MapScreenController(
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
    val viewWidth = width
    val viewHeight = height - 4
    val sectorX = worldState.getCharacter.posX / viewWidth
    val sectorY = worldState.getCharacter.posY / viewHeight
    clearOverlay()
    val terrainMap = worldState.getTerrainMap
    for (column <- 0 until viewWidth) {
      for (row <- 0 until viewHeight) {
        val terrainEntity =
          terrainMap.getEntityAt(column + sectorX * viewWidth, row + sectorY * viewHeight)
        val charToSet = terrainEntity match {
          case WallMapEntity => '#'
          case FloorMapEntity => '.'
        }
        overlay.setCharacterAt(Position.of(column, row), charToSet)
        overlay.resetColorsAndModifiers()
      }
    }
    worldState.getMobs.foreach { mob =>
      overlay.setBackgroundColor(ANSITextColor.RED)
      if (mob.posX / viewWidth == sectorX && mob.posY / viewHeight == sectorY) {
        overlay.setCharacterAt(Position.of(mob.posX % viewWidth, mob.posY % viewHeight), 'x')
      }
    }
    val character = worldState.getCharacter
    overlay.setBackgroundColor(ANSITextColor.GREEN)
    overlay.setCharacterAt(
      Position.of(character.posX % viewWidth, character.posY % viewHeight), '*')
    overlay.setBackgroundColor(ANSITextColor.YELLOW)
    overlay.putText(worldState.getLastTimeStepMessage, Position.of(0, height - 3))
    overlay.resetColorsAndModifiers()
    putStatsOnOverlay(worldState.getCharacter)
    screen.display()
  }

  private def putStatsOnOverlay(character: PlayerCharacter): Unit = {
    val charStats = character.getStats
    val currentHealth = character.currentHealth

    val row = healthHeader.getPosition.getRow

    val healthColumn = healthHeader.getPosition.component1() + healthHeader.getText.length + 1
    val healthLabel = s"$currentHealth/${charStats.health}"
    overlay.putText(healthLabel, Position.of(healthColumn, row))

    val armorColumn = armorHeader.getPosition.component1() + armorHeader.getText.length + 1
    val armorLabel = s"${charStats.armor}"
    overlay.putText(armorLabel, Position.of(armorColumn, row))

    val attackColumn = attackHeader.getPosition.component1() + attackHeader.getText.length + 1
    val attackLabel = s"${charStats.attack}"
    overlay.putText(attackLabel, Position.of(attackColumn, row))
  }
}
