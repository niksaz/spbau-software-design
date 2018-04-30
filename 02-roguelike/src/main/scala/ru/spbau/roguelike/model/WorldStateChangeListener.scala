package ru.spbau.roguelike.model

trait WorldStateChangeListener {
  def receiveWorldState(worldState: WorldState)
}
