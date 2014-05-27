package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import java.awt.Dimension

case class Preview(grid: Grid)
case class UpdateMessage(msg: String)

class Display(panel: MainPanel) extends Actor with ActorLogging {

  override def receive = {
    case Preview(grid) =>
      val panelSize: Dimension = panel.peer.getSize
      // TODO preserve scale
      val time = System.nanoTime
      val img = ImageBuilder.preview(grid, panelSize.width, panelSize.height)
      log.debug(s"previewTime: ${"%.2f" format ((System.nanoTime() - time) / 1E6)} ms")
      panel.updateImage(img)
      panel.repaint()
    case UpdateMessage(msg) =>
      panel.updateMessage(msg)
  }
}
