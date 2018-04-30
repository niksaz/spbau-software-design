package ru.spbau.roguelike.model

trait WorldStateChangeListener {
  def worldStateUpdated(): Unit
}
