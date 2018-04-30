package ru.spbau.roguelike.controller

import java.util.function.Consumer

import org.codetome.zircon.api.input.Input

trait InputListener extends Consumer[Input]
