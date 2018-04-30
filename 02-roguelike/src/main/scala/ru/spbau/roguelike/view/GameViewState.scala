package ru.spbau.roguelike.view

sealed class GameViewState

case class InActionState() extends GameViewState

case class InInventoryState() extends GameViewState
