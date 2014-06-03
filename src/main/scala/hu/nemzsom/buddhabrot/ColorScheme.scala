package hu.nemzsom.buddhabrot

import java.awt.Color
import scala.annotation.tailrec

class ColorScheme private (flags: Seq[ColorFlag]) {

  require(flags.size > 0)
  val first = flags.head
  val last = flags.last

  def color(x: Double): Color = {
    if (x <= first.loc) first.color
    else if (x >= last.loc) last.color
    else { // x is between first and last
      val (ColorFlag(fromC, fromLoc), ColorFlag(toC, toLoc)) = neighbours(x)
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

  def add(color: Color, loc: Double) =
    new ColorScheme((ColorFlag(color, loc) +: flags).sortBy(_.loc))
}

object ColorScheme {

  def apply(color: Color, loc: Double) = new ColorScheme(List(ColorFlag(color, loc)))
}

case class ColorFlag(color: Color, loc: Double) {
  require(loc >= 0.0 && loc <= 1.0, "loc should be between 0.0 to 1.0 inclusive")
}
