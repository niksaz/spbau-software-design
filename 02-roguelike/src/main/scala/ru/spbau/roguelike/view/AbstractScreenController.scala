package ru.spbau.roguelike.view

import org.codetome.zircon.api.builder.TerminalBuilder
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

  def redraw(): Unit
}
