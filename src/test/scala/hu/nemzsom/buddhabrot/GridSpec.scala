package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}
import java.nio.file.Files
import hu.nemzsom.buddhabrot.util.FileUtil

class GridSpec extends FlatSpec with Matchers {

  "Grid" should "create data based on width and height" in {
    val grid = new Grid(2, 5, 0.0, 1.0, 0.0, 1.0)
    grid.size shouldBe 10
    grid.forall(_ == 0) shouldBe true
    grid.fold(0) ((sum, _) => sum + 1 ) shouldBe 10
  }

  it should "register complex numbers in borders" in {
    val grid = new Grid(10, 5, 0.0, 5.0, -5.0, 5.0)
    grid.register(Complex(2.5, -3))
    grid.get(2, 2) shouldBe 1
    grid.register(Complex(2, -2.5))
    grid.get(2, 2) shouldBe 2
    grid.register(Complex(3, -2.5))
    grid.get(2, 3) shouldBe 1
    grid.register(Complex(2.5, -2))
    grid.get(3, 2) shouldBe 1
    grid.register(Complex(0, 0))
    grid.get(5, 0) shouldBe 1
    grid.register(Complex(2.5, 0))
    grid.get(5, 2) shouldBe 1
    grid.register(Complex(1, 2))
    grid.get(7, 1) shouldBe 1
    grid.register(Complex(2, 2))
    grid.get(7, 2) shouldBe 1
    grid.register(Complex(3, 2))
    grid.get(7, 3) shouldBe 1
    grid.register(Complex(3.5, 2))
    grid.get(7, 3) shouldBe 2
    grid.register(Complex(3, 2.5))
    grid.get(7, 3) shouldBe 3
    grid.register(Complex(1, 3))
    grid.get(8, 1) shouldBe 1
    grid.register(Complex(2, 3))
    grid.get(8, 2) shouldBe 1
    grid.toList shouldBe List(
      0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
      0, 0, 2, 1, 0, 1, 0, 1, 1, 0,
      0, 0, 1, 0, 0, 0, 0, 3, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    )
  }

  it should "register complex numbers inside" in {
    val grid = new Grid(3, 4, -1.0, 1.0, -1.0, 1.0)
    grid.register(Complex(-0.75, -0.7))
    grid.get(0, 0) shouldBe 1
    grid.register(Complex(-0.3, -0.2))
    grid.get(1, 1) shouldBe 1
    grid.register(Complex(-0.3, 0.07))
    grid.get(1, 1) shouldBe 2
    grid.register(Complex(-0.1, -0.21))
    grid.get(1, 1) shouldBe 3
    grid.register(Complex(0.25, -0.9))
    grid.get(0, 2) shouldBe 1
    grid.register(Complex(0.4, -0.4))
    grid.get(0, 2) shouldBe 2
    grid.register(Complex(0.05, 0.98))
    grid.get(2, 2) shouldBe 1
    grid.register(Complex(0.3, 0.99))
    grid.get(2, 2) shouldBe 2
    grid.register(Complex(0.7, -0.298))
    grid.get(1, 3) shouldBe 1
    grid.register(Complex(0.6, 0))
    grid.get(1, 3) shouldBe 2
    grid.register(Complex(0.97, 0.27))
    grid.get(1, 3) shouldBe 3
    grid.toList shouldBe List(
      1, 0, 0,
      0, 3, 0,
      2, 0, 2,
      0, 3, 0
    )
  }

  it should "be serializable" in {
    val grid = new Grid(10, 8, 0.0, 8.0, 0.0, 10.0)
    grid.register(Complex(5.5, 4.5))
    val path = Files.createTempFile("grid_", ".test")
    FileUtil.write(grid, path)
    FileUtil.read(path) shouldEqual grid
    Files.delete(path)
  }

  def print(grid: Grid): Unit =
    println(grid.mkString.grouped(grid.width).map(_.mkString(", ")).mkString("\n"))
}
