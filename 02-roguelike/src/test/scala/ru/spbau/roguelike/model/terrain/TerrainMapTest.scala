package ru.spbau.roguelike.model.terrain

import org.scalatest.FunSuite

class TerrainMapTest extends FunSuite {

  test("containsOnePassableCell") {
    val size = 3
    val terrainMap = new TerrainMap(size, size)
    assert(terrainMap.isPassable(1, 1))
  }

  test("testWallsAround") {
    val size = 10
    val terrainMap = new TerrainMap(size, size)
    for (i <- Range(0, size)) {
      assert(terrainMap.getEntityAt(i, 0) == WallMapEntity)
      assert(terrainMap.getEntityAt(i, size - 1) == WallMapEntity)
      assert(terrainMap.getEntityAt(0, i) == WallMapEntity)
      assert(terrainMap.getEntityAt(size - 1, i) == WallMapEntity)
    }
  }

  test("cellOutsideIsNotPassable") {
    val size = 10
    val terrainMap = new TerrainMap(size, size)
    assert(!terrainMap.isPassable(-1, -1))
    assert(!terrainMap.isPassable(0, size))
    assert(!terrainMap.isPassable(size, 0))
    assert(!terrainMap.isPassable(size, size))
  }
}
