package ru.spbau.roguelike.view

import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.codetome.zircon.api.terminal.Terminal
import ru.spbau.roguelike.model.{WorldState, WorldStateChangeListener}
import ru.spbau.roguelike.view.action.{ActionScreenController, ActionScreenListener}
import ru.spbau.roguelike.view.inventory.{InventoryScreenController, InventoryScreenListener}

class GameView(worldState: WorldState) extends WorldStateChangeListener {

  private val terminal: Terminal =
    TerminalBuilder.newBuilder
      .initialTerminalSize(
        Size.of(
          worldState.getWorldMap.width,
          worldState.getWorldMap.height + 4))
      .font(CP437TilesetResource.ROGUE_YUN_16X16.toFont())
      .title(GameView.GAME_VIEW_TITLE)
      .build()

  private val actionScreenController = new ActionScreenController(worldState, terminal)
  private val actionScreenListener = new ActionScreenListener(worldState, this)

  private val inventoryScreenController = new InventoryScreenController(worldState, terminal)
  private val inventoryScreenListener =
    new InventoryScreenListener(worldState, this, inventoryScreenController)

  private var gameViewState: GameViewState = InActionState

  {
    worldState.addChangeListener(this)
    terminal.onInput { input =>
      gameViewState match {
        case InActionState => actionScreenListener.accept(input)
        case InInventoryState => inventoryScreenListener.accept(input)
        case null =>
      }
    }
  }

  override def worldStateUpdated(): Unit = {
    redrawCurrentScreen()
  }

  def show(): Unit = {
    redrawCurrentScreen()
  }

  def changeGameViewStateTo(gameViewState: GameViewState): Unit = {
    this.gameViewState = gameViewState
    redrawCurrentScreen()
  }

  def redrawCurrentScreen(): Unit = gameViewState match {
    case InActionState => actionScreenController.redraw()
    case InInventoryState => inventoryScreenController.redraw()
    case null =>
  }
}

object GameView {
  private val GAME_VIEW_TITLE = "Roguelike"
}
