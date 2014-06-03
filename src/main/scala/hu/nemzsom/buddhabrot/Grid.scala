package hu.nemzsom.buddhabrot

class Grid(val width: Int, val height: Int, val reFrom: Double, val reTo: Double, val imFrom: Double, val imTo: Double) extends Iterable[Int] with Serializable {

  require(width > 0)
  require(height > 0)
  require(reTo > reFrom)
  require(imTo > imFrom)

  val reDelta = (reTo - reFrom) / height
  val imDelta = (imTo - imFrom) / width

  override val size = width * height

  val data = new Array[Int](size)

  override def iterator: Iterator[Int] = data.iterator

  def indexFor(x: Int, y: Int) = y * width + x

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

  override def canEqual(other: Any): Boolean = other.isInstanceOf[Grid]

  override def equals(other: Any): Boolean = other match {
    case that: Grid =>
      (that canEqual this) &&
        reDelta == that.reDelta &&
        imDelta == that.imDelta &&
        size == that.size &&
        data.sameElements(that.data) &&
        width == that.width &&
        height == that.height &&
        reFrom == that.reFrom &&
        reTo == that.reTo &&
        imFrom == that.imFrom &&
        imTo == that.imTo
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(reDelta, imDelta, size, data, width, height, reFrom, reTo, imFrom, imTo)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString(): String = {
    s"grid[width: $width, height: $height, reFrom: $reFrom, reTo: $reTo, imFrom: $imFrom, imTo: $imTo]"
  }
}
