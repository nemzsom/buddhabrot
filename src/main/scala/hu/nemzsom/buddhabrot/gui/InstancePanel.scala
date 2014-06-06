package hu.nemzsom.buddhabrot.gui

import scala.swing.FlowPanel
import scala.swing.FlowPanel.Alignment
import scala.swing.Swing._
import java.awt.Color
import hu.nemzsom.buddhabrot.util.ImageUtil
import ImageUtil._
import hu.nemzsom.buddhabrot.Config

class InstancePanel(conf: Config) extends FlowPanel(Alignment.Left)() {

  val (w, h) = getTargetDimension(conf.width, conf.height, 200, 200)

  preferredSize = (w + 25, h * conf.instances.size)
  focusable = true

  conf.instances foreach { _ =>
    contents += new ImgPanel(w, h)
  }

}
