package hu.nemzsom.buddhabrot

import java.awt.Color
import scala.annotation.tailrec
import java.util.UUID

class ColorScheme (_flags: Seq[ColorFlag]) {

  require(_flags.size > 0)
  require(_flags.map(_.id).distinct.size == _flags.size)
  val flags = _flags.sortBy(_.loc)
  val first = flags.head
  val last = flags.last

  def color(x: Double): Color = {
    if (x <= first.loc) first.color
    else if (x >= last.loc) last.color
    else { // x is between first and last
      val (ColorFlag(fromC, fromLoc, _), ColorFlag(toC, toLoc, _)) = neighbours(x)
      val interpolator = interpolate(fromLoc, x, toLoc)_
      new Color(
        interpolator(fromC.getRed, toC.getRed),
        interpolator(fromC.getGreen, toC.getGreen),
        interpolator(fromC.getBlue, toC.getBlue)
      )
    }
  }

  // x must be between first and last
  private def neighbours(x: Double): (ColorFlag, ColorFlag) = {
    @tailrec
    def find(act: ColorFlag, rest: Seq[ColorFlag]): (ColorFlag, ColorFlag) = {
      if (x < rest.head.loc) (act, rest.head)
      else find(rest.head, rest.tail)
    }
    find(flags.head, flags.tail)
  }

  private def interpolate(srcFrom: Double, src: Double, srcTo: Double)(targetFrom: Int, targetTo: Int): Int = {
    val srcRatio = (src - srcFrom) / (srcTo - srcFrom)
    val tRange = targetTo - targetFrom
    targetFrom + (tRange * srcRatio).round.toInt
  }

  def add(color: Color, loc: Double): ColorScheme =
    add(ColorFlag(color, loc, UUID.randomUUID))

  def add(flag: ColorFlag) =
    new ColorScheme(flag +: flags)

  def updated(id: UUID, color: Color, loc: Double): ColorScheme =
    new ColorScheme(ColorFlag(color, loc, id) +: removeFlag(id))

  def remove(id: UUID): ColorScheme =
    new ColorScheme(removeFlag(id))

  private def removeFlag(id: UUID): Seq[ColorFlag] =
    flags.filterNot(_.id == id)

  override def toString =
    s"ColorScheme$flags"
}

object ColorScheme {

  val BLACK_TO_WHITE = ColorScheme(Color.BLACK, 0.0) add (Color.WHITE, 1.0)

  def apply(color: Color, loc: Double) = new ColorScheme(List(ColorFlag(color, loc, UUID.randomUUID)))
}

case class ColorFlag(color: Color, loc: Double, id: UUID) {
  require(loc >= 0.0 && loc <= 1.0, "loc should be between 0.0 to 1.0 inclusive")
}
