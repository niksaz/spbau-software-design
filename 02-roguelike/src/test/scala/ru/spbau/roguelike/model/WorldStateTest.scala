package ru.spbau.roguelike.model

import org.scalatest.FunSuite

class WorldStateTest extends FunSuite {

  test("constructorContracts") {
    val size = 10
    val worldState = WorldState(size, size)
    val terrainMap = worldState.getTerrainMap
    assert(terrainMap.width == size)
    assert(terrainMap.height == size)
    assert(terrainMap.isPassable(worldState.getCharacter.posX, worldState.getCharacter.posY))
    worldState.getMobs.foreach { mob =>
      assert(terrainMap.isPassable(mob.posX, mob.posY))
    }
  }

  test("addChangeListener") {
    val size = 10
    val worldState = WorldState(size, size)
    var changesReceived = 0
    worldState.addChangeListener(() => {
      changesReceived += 1
    })
    worldState.moveCharacterUp()
    assert(changesReceived == 1)
    worldState.moveCharacterDown()
    assert(changesReceived == 2)
    worldState.moveCharacterLeft()
    assert(changesReceived == 3)
    worldState.moveCharacterRight()
    assert(changesReceived == 4)
  }

  test("moveCharacterUp") {
    testMoveByDelta(0, -1, (worldState: WorldState) => worldState.moveCharacterUp())
  }

  test("moveCharacterDown") {
    testMoveByDelta(0, 1, (worldState: WorldState) => worldState.moveCharacterDown())
  }

  test("moveCharacterLeft") {
    testMoveByDelta(-1, 0, (worldState: WorldState) => worldState.moveCharacterLeft())
  }

  test("moveCharacterRight") {
    testMoveByDelta(1, 0, (worldState: WorldState) => worldState.moveCharacterRight())
  }

  private def testMoveByDelta(deltaX: Int, deltaY: Int, stateUpdater: WorldState => Unit): Unit = {
    val size = 10
    var worldState: WorldState = null
    var initPosX: Int = -1
    var initPosY: Int = -1
    do {
      worldState = WorldState(size, size, 0)
      initPosX = worldState.getCharacter.posX
      initPosY = worldState.getCharacter.posY
    } while (!worldState.getTerrainMap.isPassable(initPosX + deltaX, initPosY + deltaY))
    stateUpdater(worldState)
    assert(worldState.getCharacter.posX == initPosX + deltaX)
    assert(worldState.getCharacter.posY == initPosY + deltaY)
  }
}
