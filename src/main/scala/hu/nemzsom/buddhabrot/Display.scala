package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import java.awt.Dimension

case class Preview(grid: Grid)
case class UpdateMainMessage(msg: String)
case class UpdateSecMessage(msg: String)

class Display(panel: Panel) extends Actor with ActorLogging {

  override def receive = {
    case Preview(grid) =>
      val panelSize: Dimension = panel.peer.getSize
      val time = System.nanoTime
      val img = ImageBuilder.preview(grid, panelSize.width, panelSize.height)
      log.debug(s"previewTime: ${"%.2f" format ((System.nanoTime() - time) / 1E6)} ms")
      panel.updateImage(img)
      panel.repaint()
    case UpdateMainMessage(msg) =>
      panel.updateMainMessage(msg)
    case UpdateSecMessage(msg) =>
      panel.updateSecondaryMessage(msg)
  }
}
