package ru.spbau.roguelike.view

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.roguelike.model.{WorldState, WorldStateChangeListener}
import ru.spbau.roguelike.view.action.{ActionScreenController, ActionScreenListener}
import ru.spbau.roguelike.view.inventory.{InventoryScreenController, InventoryScreenListener}
import ru.spbau.roguelike.view.lost.LostScreenController

/**
  * An entity that creates and control the flow of the screens behind
  * [[AbstractScreenController]]s.
  * @param visibleMapWidth the width of model's map that should be visible to a user
  * @param visibleMapHeight the height of model's map that should be visible to a user
  * @param worldState the state which should be used for getting and updating the game's model
  */
class GameView(
  visibleMapWidth: Int,
  visibleMapHeight: Int,
  worldState: WorldState
) extends WorldStateChangeListener {

  private val terminal: Terminal =
    TerminalBuilder.newBuilder
      .initialTerminalSize(
        Size.of(visibleMapWidth, visibleMapHeight + 4))
      .font(CP437TilesetResource.ROGUE_YUN_16X16.toFont())
      .title(GameView.GAME_VIEW_TITLE)
      .build()

  private val actionScreenController = new ActionScreenController(worldState, terminal)
  private val actionScreenListener = new ActionScreenListener(worldState, this)

  private val inventoryScreenController = new InventoryScreenController(worldState, terminal)
  private val inventoryScreenListener =
    new InventoryScreenListener(worldState, this, inventoryScreenController)

  private val lostScreenController = new LostScreenController(worldState, terminal)

  private var gameViewState: GameViewState = InActionState

  {
    worldState.addChangeListener(this)
    terminal.onInput { input =>
      gameViewState match {
        case InActionState => actionScreenListener.accept(input)
        case InInventoryState => inventoryScreenListener.accept(input)
        case InLostState =>
      }
    }
  }

  /** Displays the current screen. */
  def display(): Unit = {
    redrawCurrentScreen()
  }

  override def worldStateUpdated(): Unit = {
    redrawCurrentScreen()
  }

  private[view] def changeGameViewStateTo(gameViewState: GameViewState): Unit = {
    this.gameViewState = gameViewState
    redrawCurrentScreen()
  }

  private def redrawCurrentScreen(): Unit = gameViewState match {
    case InActionState => actionScreenController.redraw()
    case InInventoryState => inventoryScreenController.redraw()
    case InLostState => lostScreenController.redraw()
  }
}

object GameView {
  private val GAME_VIEW_TITLE = "Roguelike"
}
