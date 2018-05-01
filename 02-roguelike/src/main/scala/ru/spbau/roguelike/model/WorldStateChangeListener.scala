package ru.spbau.roguelike.model

/**
  * An entity which is interested in listening for [[WorldState]] changes.
  * Use [[WorldState.addChangeListener()]] to subscribe.
  */
trait WorldStateChangeListener {
  def worldStateUpdated(): Unit
}
