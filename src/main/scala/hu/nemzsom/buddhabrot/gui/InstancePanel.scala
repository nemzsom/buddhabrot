package hu.nemzsom.buddhabrot.gui

import scala.swing.FlowPanel
import scala.swing.FlowPanel.Alignment
import scala.swing.Swing._
import java.awt.Color
import hu.nemzsom.buddhabrot.util.ImageUtil
import ImageUtil._
import hu.nemzsom.buddhabrot.App._

class InstancePanel extends FlowPanel(Alignment.Left)() {

  val (w, h) = getTargetDimension(config.width, config.height, 200, 200)

  preferredSize = (w + 25, h * config.instances.size)
  focusable = true
  background = Color.GRAY

  config.instances foreach { _ =>
    contents += new ImgPanel(w, h)
  }

}
