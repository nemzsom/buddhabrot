package hu.nemzsom.buddhabrot

import akka.actor.Actor

case class Update(data: Iterable[Int])

class Display(panel: MainPanel) extends Actor {

  val scale = Display.scale(0, 255)_
  var min = 0
  var max = 255
  
  override def receive = {
    case Update(data) =>
      val pixels = panel.data
      val actScale = scale(min, max)(_)
      min = Integer.MAX_VALUE
      data.zipWithIndex foreach { case (count, i) =>
        if (count > max) max = count
        if (count < min) min = count
        val n = actScale(count)
        pixels(i) = n | n << 8 | n << 16
      }
      panel.repaint()
  }
}

object Display {

  def scale(rangeFrom: Int, rangeTo: Int)(min: Int, max: Int)(x: Int): Int = {
    if (x < min) rangeFrom
    else if (x > max) rangeTo
    else {
      val targetSize = rangeTo - rangeFrom
      val sourceSize = max - min
      val scale = targetSize.toDouble / sourceSize
      val result = (scale * (x - min)).round.toInt + rangeFrom
      if (result < rangeFrom) rangeFrom
      else result
    }
  }
}
