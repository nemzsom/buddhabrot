package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}

class GridSpec extends FlatSpec with Matchers {

  "Grid" should "create data based on width and height" in {
    val g = new Grid(2, 5, 0.0, 1.0, 0.0, 1.0)
    g.size shouldBe 10
    g.fold(0) ((sum, _) => sum + 1 ) shouldBe 10
  }

  val grid = new Grid(10, 5, -2.0, 3.0, -1.0, 1.0)

  it should "register complex numbers" in {
    grid.forall(_ == 0) shouldBe true
    grid.register(Complex(-2, -1))
    grid.get(0, 0) shouldBe 1
    grid.register(Complex(0, -0.8))
    println(grid.mkString.grouped(10).map(_.mkString(", ")).mkString("\n"))
    //grid.get(1, 2) shouldBe 1
    grid.register(Complex(-1.5, 0.4))
    //grid.get(7, 0) shouldBe 1
    grid.register(Complex(-1.5, 0.5))
    //grid.get(7, 0) shouldBe 2
    grid.register(Complex(1.5, 0.3))
    //grid.get(6, 3) shouldBe 1
    println("\n---\n")
    println(grid.mkString.grouped(10).map(_.mkString(", ")).mkString("\n"))
    grid.toList shouldBe List(
      1, 0, 0, 0, 0, 0, 0, 2, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    )
  }
}
