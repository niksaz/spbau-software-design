package ru.spbau.roguelike.view

sealed trait GameViewState

case object InActionState extends GameViewState

case object InInventoryState extends GameViewState
