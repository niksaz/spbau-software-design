package ru.spbau.roguelike.view.lost

import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.component.builder.PanelBuilder
import org.codetome.zircon.api.resource.ColorThemeResource
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.api.{Position, Size}
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.AbstractScreenController

class LostScreenController(
  worldState: WorldState,
  terminal: Terminal
) extends AbstractScreenController(worldState, terminal) {

  {
    screen.setCursorVisibility(false)

    val panel =
      PanelBuilder.newBuilder()
        .wrapWithBox()
        .wrapWithShadow()
        .size(Size.of(width - 2, height - 2))
        .position(Position.OFFSET_1x1)
        .build()
    val colorTheme = ColorThemeResource.ADRIFT_IN_DREAMS.getTheme
    panel.applyColorTheme(colorTheme)

    screen.addComponent(panel)
  }

  override def redraw(): Unit = {
    overlay.setBackgroundColor(ANSITextColor.RED)
    overlay.putText("You have died. :|", Position.of(3, 3))
    overlay.resetColorsAndModifiers()
    screen.display()
  }
}
