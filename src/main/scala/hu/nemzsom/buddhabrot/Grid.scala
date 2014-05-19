package hu.nemzsom.buddhabrot

class Grid(width: Int, height: Int, reFrom: Double, reTo: Double, imFrom: Double, imTo: Double) extends Iterable[Int] {

  require(width > 0)
  require(height > 0)
  require(reTo > reFrom)
  require(imTo > imFrom)

  val reDelta = (reTo - reFrom) / height
  val imDelta = (imTo - imFrom) / width

  override val size = width * height

  private val data = new Array[Int](size)

  override def iterator = data.iterator

  private def indexFor(x: Int, y: Int) = y * width + x

  def get(x: Int, y: Int) = data(indexFor(x, y))

  private def inc(x: Int, y: Int): Unit = {
    val i = indexFor(x, y)
    data(i) = data(i) + 1
  }

  def register(c: Complex) = {
    def index(from: Double, to: Double, k: Double, delta: Double): Int = {
      if (k < from || k >= to) -1
      else ((k - from) / delta).toInt
    }
    val x = index(imFrom, imTo, c.im, imDelta)
    val y = index(reFrom, reTo, c.re, reDelta)
    if (x >= 0 && y >= 0) inc(x, y)
  }
}
