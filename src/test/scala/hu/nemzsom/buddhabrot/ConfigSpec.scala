package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}

class ConfigSpec extends FlatSpec with Matchers {

  "Config" should "calculate 'imTo' correctly" in {
    Config(1, 1, 0.0, 1.0, 0.0, 0).reTo shouldBe 1.0
    Config(2, 1, 0.0, 2.0, 0.0, 0).reTo shouldBe 1.0
    Config(2, 1, -0.5, 0.5, -0.2, 0).reTo shouldBe 0.3
  }
}
