package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}
import java.awt.Color

class ConfigSpec extends FlatSpec with Matchers {

  "Config" should "calculate 'imTo' correctly" in {
    config(1, 1, 0.0, 1.0, 0.0).reTo shouldBe 1.0
    config(2, 1, 0.0, 2.0, 0.0).reTo shouldBe 1.0
    config(2, 1, -0.5, 0.5, -0.2).reTo shouldBe 0.3
  }

  it should "throw exception if rgbWeights not sum to 1.0" in {
    intercept[IllegalArgumentException] {
      Config(0, 0, 0.0, 1.0, 0.0, "", Scale.linear _,
        Variation(
          0, 0, Color.WHITE,
          (0.5, 0.5, 0.5)
        ),
        Variation(
          0, 0, Color.WHITE,
          (0.5, 0.4, 0.5)
        )
      )
    }
  }

  def config(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double) =
    Config(width, height, imFrom, imTo, reFrom, "", Scale.linear _, Variation(0, 0, Color.WHITE, (1.0, 1.0, 1.0)))
}
