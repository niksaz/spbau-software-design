package ru.spbau.roguelike.model

import org.scalatest.FunSuite
import ru.spbau.roguelike.model.MapDirection.{EastDirection, NorthDirection, SouthDirection, WestDirection}

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
    worldState.moveCharacter(NorthDirection)
    assert(changesReceived == 1)
    worldState.moveCharacter(SouthDirection)
    assert(changesReceived == 2)
    worldState.moveCharacter(WestDirection)
    assert(changesReceived == 3)
    worldState.moveCharacter(EastDirection)
    assert(changesReceived == 4)
  }

  test("moveCharacterNorth") {
    testMoveByDirection(NorthDirection)
  }

  test("moveCharacterSouth") {
    testMoveByDirection(SouthDirection)
  }

  test("moveCharacterWest") {
    testMoveByDirection(WestDirection)
  }

  test("moveCharacterEast") {
    testMoveByDirection(EastDirection)
  }

  private def testMoveByDirection(dir: MapDirection.Direction): Unit = {
    val size = 10
    var worldState: WorldState = null
    var initPosX: Int = -1
    var initPosY: Int = -1
    do {
      worldState = WorldState(size, size, 0)
      initPosX = worldState.getCharacter.posX
      initPosY = worldState.getCharacter.posY
    } while (!worldState.getTerrainMap.isPassable(initPosX + dir.deltaX, initPosY + dir.deltaY))
    worldState.moveCharacter(dir)
    assert(worldState.getCharacter.posX == initPosX + dir.deltaX)
    assert(worldState.getCharacter.posY == initPosY + dir.deltaY)
  }
}
