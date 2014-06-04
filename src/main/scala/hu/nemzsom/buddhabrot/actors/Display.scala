package hu.nemzsom.buddhabrot.actors

import akka.actor.{ActorLogging, Actor}
import java.awt.Dimension
import hu.nemzsom.buddhabrot.gui.ImgPanel
import hu.nemzsom.buddhabrot.Grid
import hu.nemzsom.buddhabrot.Instance

case class Preview(grids: Seq[(Grid, Instance)])
case class UpdateMainMessage(msg: String)
case class UpdateSecMessage(msg: String)

class Display(panel: ImgPanel) extends Actor with ActorLogging {

  override def receive = {
    case Preview(grids) =>
      /*val panelSize: Dimension = panel.peer.getSize
      val img = ImageBuilder.preview(grids, panelSize.width, panelSize.height)
      panel.updateImage(img)
      panel.repaint()*/
    case UpdateMainMessage(msg) =>
      panel.updateMessage(msg)
  }
}
