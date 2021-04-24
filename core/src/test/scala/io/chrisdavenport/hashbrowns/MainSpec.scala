package io.chrisdavenport.hashbrowns

import munit.CatsEffectSuite
import cats.effect._

class MainSpec extends CatsEffectSuite {

  test("Main should exit succesfully") {
    assert(clue(true) == clue(true))
  }

}
