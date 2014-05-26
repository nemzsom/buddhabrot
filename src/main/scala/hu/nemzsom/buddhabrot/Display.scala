package hu.nemzsom.buddhabrot

import akka.actor.Actor
import hu.nemzsom.buddhabrot.Main._

case class UpdatePixels(data: Iterable[Int])
case class UpdateMessage(msg: String)
case object SaveImg

class Display(panel: MainPanel) extends Actor {

  val scale = Display.scale(0, 255)_
  var min = 0
  var max = 0
  
  override def receive = {
    case UpdatePixels(data) =>
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
    case UpdateMessage(msg) =>
      panel.updateMessage(msg)
    case SaveImg =>
      panel.updateMessage("Saving image...")
      val savedImg = new ImageSaver(config.outDir).saveImage(panel.img)
      panel.updateMessage(s"Image saved to $savedImg.")
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
