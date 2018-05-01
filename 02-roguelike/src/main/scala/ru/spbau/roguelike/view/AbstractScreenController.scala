package ru.spbau.roguelike.view

import org.codetome.zircon.api.builder.{LayerBuilder, TerminalBuilder, TextCharacterBuilder}
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.screen.Screen
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.roguelike.model.WorldState

abstract class AbstractScreenController(
  protected val worldState: WorldState,
  protected val terminal: Terminal
) {
  protected val height: Int = terminal.getBoundableSize.getRows
  protected val width: Int = terminal.getBoundableSize.getColumns

  protected val screen: Screen = TerminalBuilder.createScreenFor(terminal)

  protected val overlay: Layer =
    new LayerBuilder()
      .size(screen.getBoundableSize)
      .filler(TextCharacterBuilder.EMPTY)
      .build

  def redraw(): Unit

  {
    screen.pushLayer(overlay)
  }
}
