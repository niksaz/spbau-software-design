package ru.spbau.roguelike.model

import ru.spbau.roguelike.model.CharacterBodyPart.CharacterBodyPart

case class Item(name: String, stats: GameStats, bodyPart: CharacterBodyPart)
