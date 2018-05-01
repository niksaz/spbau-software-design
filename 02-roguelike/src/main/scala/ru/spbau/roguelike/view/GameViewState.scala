package ru.spbau.roguelike.view

/** Represents the state/screen that should be shown according to [[GameView]]. */
private[view] sealed trait GameViewState

/** State for [[ru.spbau.roguelike.view.action.ActionScreenController]]'s screen. */
private[view] case object InActionState extends GameViewState

/** State for [[ru.spbau.roguelike.view.inventory.InventoryScreenController]]'s screen. */
private[view] case object InInventoryState extends GameViewState

/** State for [[ru.spbau.roguelike.view.lost.LostScreenController]]'s screen. */
private[view] case object InLostState extends GameViewState
