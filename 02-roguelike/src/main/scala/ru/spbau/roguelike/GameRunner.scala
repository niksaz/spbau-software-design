package ru.spbau.roguelike

import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.GameView

object GameRunner {
  def main(args: Array[String]): Unit = {
    val width = 48
    val height = 20

    val worldState = WorldState(width, height)

    val gameView = new GameView(worldState)

    gameView.show()
  }
}
