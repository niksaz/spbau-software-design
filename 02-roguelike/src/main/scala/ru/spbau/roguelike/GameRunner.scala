package ru.spbau.roguelike

import com.typesafe.scalalogging.Logger
import ru.spbau.roguelike.model.WorldState
import ru.spbau.roguelike.view.GameView

object GameRunner {
  private val logger = Logger("ru.spbau.roguelike.GameRunner")

  def main(args: Array[String]): Unit = {
    Thread.setDefaultUncaughtExceptionHandler((t: Thread, e: Throwable) => {
      logger.error(e.toString)
      e.getStackTrace.foreach { stackTraceElement =>
         logger.error(stackTraceElement.toString)
      }
    })

    val width = 48
    val height = 20

    val worldState = WorldState(2 * width, 2 * height)

    val gameView = new GameView(width, height, worldState)

    gameView.show()
  }
}
