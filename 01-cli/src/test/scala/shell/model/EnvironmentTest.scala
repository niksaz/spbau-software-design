package shell.model

import org.scalatest.FunSuite

import scala.reflect.io.Path

class EnvironmentTest extends FunSuite {
  private val testPath = Path("")

  test("updateVariable") {
    var environment = Environment(testPath)
    environment = environment.updateVariable("x", "15")
    assert(environment.retrieveVariable("x") == "15")
  }

  test("defaultValue") {
    var environment = Environment(testPath)
    environment = environment.updateVariable("x", "15")
    assert(environment.retrieveVariable("y").isEmpty)
  }
}