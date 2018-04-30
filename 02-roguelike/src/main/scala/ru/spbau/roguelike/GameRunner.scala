package ru.spbau.roguelike

import ru.spbau.roguelike.controller.InputListenerImpl
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.GameScreen

object GameRunner {
  def main(args: Array[String]): Unit = {
    val width = 48
    val height = 20

    val worldState = WorldState(width, height)

    val screen = new GameScreen(width, height)
    val inputListener = new InputListenerImpl(worldState)
    screen.addOnInputListener(inputListener)

    worldState.addChangeListener(screen)
    worldState.notifyChangeListeners()
  }
}
