package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}
import java.awt.Color

class ColorSchemeSpec extends FlatSpec with Matchers {

  /**   0.2   0.5  0.7   1.0
   * r  100   110  100   255
   * g  100   150  100   0
   * b  100   200  100   100
   */
  val scheme = {
    var s = ColorScheme(new Color(110, 150, 200), 0.5)
    s = s.add(new Color(100, 100, 100), 0.2)
    s = s.add(new Color(100, 100, 100), 0.7)
    s.add(new Color(255, 0, 100), 1.0)
  }

  "ColorScheme" should "map one color in case of one flag" in {
    val color = new Color(0, 100, 200)
    def test(sch: ColorScheme): Unit = {
      sch.color(0.0) shouldBe color
      sch.color(0.5) shouldBe color
      sch.color(0.752) shouldBe color
      sch.color(1.0) shouldBe color
    }
    test(ColorScheme(color, 0.0))
    test(ColorScheme(color, 0.1))
    test(ColorScheme(color, 0.5))
    test(ColorScheme(color, 0.2))
  }

  it should "map colors at flags" in {
    scheme.color(0.0) shouldBe new Color(100, 100, 100)
    scheme.color(0.2) shouldBe new Color(100, 100, 100)
    scheme.color(0.5) shouldBe new Color(110, 150, 200)
    scheme.color(0.7) shouldBe new Color(100, 100, 100)
    scheme.color(1.0) shouldBe new Color(255, 0, 100)
  }

  it should "map colors between flags" in {
    scheme.color(0.1) shouldBe new Color(100, 100, 100)
    scheme.color(0.3) shouldBe new Color(103, 117, 133)
    scheme.color(0.6) shouldBe new Color(105, 125, 150)
    scheme.color(0.85) shouldBe new Color(178, 50, 100)
  }

}
