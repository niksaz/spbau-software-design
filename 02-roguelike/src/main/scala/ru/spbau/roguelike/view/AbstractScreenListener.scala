package ru.spbau.roguelike.view

import java.util.function.Consumer

import org.codetome.zircon.api.input.Input
import ru.spbau.roguelike.model.WorldState

/**
  * A base class for a listener of a screen based on [[org.codetome.zircon.api.terminal.Terminal]].
  */
abstract class AbstractScreenListener(
  protected val worldState: WorldState,
  protected val gameView: GameView
) extends Consumer[Input]
