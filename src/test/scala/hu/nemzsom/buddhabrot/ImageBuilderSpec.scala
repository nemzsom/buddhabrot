package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}

class ImageBuilderSpec extends FlatSpec with Matchers {

  /*import ImageBuilder._

  // 2 | 2 | 4 | 2
  // 2 | 7 | 2 | 8
  val grid = new Grid(4, 2, 0.0, 2.0, 0.0, 4.0)
  for {
    x <- 0 to 3
    y <- 0 to 1
  } {
    (1 to 2) foreach (_ => grid.register(Complex(y + 0.5, x + 0.5)))
  }
  (1 to 2) foreach (_ => grid.register(Complex(0.5, 2.5)))
  (1 to 5) foreach (_ => grid.register(Complex(1.5, 1.5)))
  (1 to 6) foreach (_ => grid.register(Complex(1.5, 3.5)))
  // -------------

  "minMax function" should "calculate minimum and maximum correctly" in {
    val (min, max) = minMax(List(0, 5, 8, -2, 3, 87, 45))
    min shouldBe -2
    max shouldBe 87
  }

  "build" should "produce correct image" in {
    val img = build(grid)
    val rgbs = getRgbs(img.pixels)
    rgbs foreach { case (r, g, b) =>
      r shouldEqual g
      g shouldEqual b
    }
    rgbs map (_._1) shouldEqual List(
      0,   0,  85,   0,
      0, 213,   0, 255
    )
  }

  "preview" should "produce correct image" in {
    val img = preview(grid, 2, 1)
    val rgbs = getRgbs(img.pixels)
    rgbs foreach { case (r, g, b) =>
      r shouldEqual g
      g shouldEqual b
    }
    rgbs map (_._1) shouldEqual List(0, 85)
  }

  "getTargetDimension" should "calculate Dimensions correctly" in {
    // width greater than height
    {
      val sourceWidth = 8
      val sourceHeight = 4
      getTargetDimension(sourceWidth, sourceHeight, 4, 2) shouldBe (4, 2)
      getTargetDimension(sourceWidth, sourceHeight, 2, 4) shouldBe (2, 1)
      getTargetDimension(sourceWidth, sourceHeight, 2, 2) shouldBe (2, 1)
      getTargetDimension(sourceWidth, sourceHeight, 8, 2) shouldBe (4, 2)
      getTargetDimension(sourceWidth, sourceHeight, 2, 8) shouldBe (2, 1)
    }
    // height greater than width
    {
      val sourceWidth = 4
      val sourceHeight = 8
      getTargetDimension(sourceWidth, sourceHeight, 4, 2) shouldBe (1, 2)
      getTargetDimension(sourceWidth, sourceHeight, 2, 4) shouldBe (2, 4)
      getTargetDimension(sourceWidth, sourceHeight, 2, 2) shouldBe (1, 2)
      getTargetDimension(sourceWidth, sourceHeight, 8, 2) shouldBe (1, 2)
      getTargetDimension(sourceWidth, sourceHeight, 2, 8) shouldBe (2, 4)
    }

  }

  def getRgbs(pixels: Array[Int]): Array[(Int, Int, Int)] =
    pixels.map(n => (0xFF & n >> 16, 0xFF & n >> 8, 0xFF & n))*/
}
