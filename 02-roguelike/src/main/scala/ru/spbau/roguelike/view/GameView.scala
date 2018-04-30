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

  private var gameViewState: GameViewState = _

  private val actionScreenController = new ActionScreenController(worldState, terminal)
  private val actionScreenListener = new ActionScreenListener(worldState, this)

  private val inventoryScreenController = new InventoryScreenController(worldState, terminal)
  private val inventoryScreenListener =
    new InventoryScreenListener(worldState, this, inventoryScreenController)

  {
    worldState.addChangeListener(this)
    terminal.onInput { input =>
      gameViewState match {
        case InActionState() => actionScreenListener.accept(input)
        case InInventoryState() => inventoryScreenListener.accept(input)
        case _ =>
      }
    }
  }
  override def worldStateUpdated(): Unit = gameViewState match {
    case InActionState() => actionScreenController.redraw()
    case InInventoryState() => inventoryScreenController.redraw()
    case _ =>
  }

  def show(): Unit = {
    changeGameViewStateTo(InActionState())
  }

  def changeGameViewStateTo(gameViewState: GameViewState): Unit = {
    this.gameViewState = gameViewState
    worldStateUpdated()
  }
}

object GameView {
  private val GAME_VIEW_TITLE = "Roguelike"
}
