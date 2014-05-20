package hu.nemzsom.buddhabrot

import akka.actor.Actor

case class Update(data: Iterable[Int])

class Display(panel: ImagePanel) extends Actor {
  
  override def receive = {
    case Update(data) =>
      var min = Integer.MAX_VALUE
      var max = 0
      data.foreach { k =>
        if (k < min) min = k
        if (k > max) max = k
      }
      val scale = (255 + min) / max
      val pixels = panel.data
      data.zipWithIndex foreach { case (count, i) =>
        val scaled = scale / count - min
        pixels(i) = scaled
      }
      panel.repaint()
  }
}
