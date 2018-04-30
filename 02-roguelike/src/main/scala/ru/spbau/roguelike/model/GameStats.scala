package ru.spbau.roguelike.model

case class GameStats(var health: Int, var attack: Int, var defence: Int) {
  def +(that: GameStats) =
    GameStats(health + that.health, attack + that.attack, defence + that.defence)
}
