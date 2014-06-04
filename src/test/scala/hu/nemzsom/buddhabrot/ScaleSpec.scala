package hu.nemzsom.buddhabrot

import org.scalatest.{FlatSpec, Matchers}
import hu.nemzsom.buddhabrot.util.Scale

class ScaleSpec extends FlatSpec with Matchers {

  "Linear scale" should "scale to range [0..10]" in {
    val sc = Scale.linear(0, 10)(0, 100)(_)
    sc(0) shouldBe 0
    sc(100) shouldBe 10
    sc(40) shouldBe 4
    40 to 44 foreach (sc(_) shouldBe 4)
    45 to 50 foreach (sc(_) shouldBe 5)
  }

  it should "scale to range [100..200]" in {
    val sc = Scale.linear(100, 200)(0, 1000)(_)
    sc(0) shouldBe 100
    sc(1000) shouldBe 200
    sc(100) shouldBe 110
    sc(494) shouldBe 149
    495 to 504 foreach (sc(_) shouldBe 150)
    sc(505) shouldBe 151
  }

  it should "scale to zero if max is zero" in {
    val sc = Scale.linear(0, 255)(0, 0)(_)
    sc(0) shouldBe 0
  }

  it should "scale to boundary if x out of bounds" in {
    val sc = Scale.linear(0, 255)(300, 1000)(_)
    sc(0) shouldBe 0
    sc(200) shouldBe 0
    sc(300) shouldBe 0
    sc(1000) shouldBe 255
    sc(1001) shouldBe 255
    sc(2000) shouldBe 255
  }
}
