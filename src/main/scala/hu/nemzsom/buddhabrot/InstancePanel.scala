package hu.nemzsom.buddhabrot

import scala.swing.FlowPanel
import scala.swing.FlowPanel.Alignment
import scala.swing.Swing._
import App.config
import java.awt.Color
import ImageUtil.getTargetDimension

class InstancePanel extends FlowPanel(Alignment.Left)() {

  val (w, h) = getTargetDimension(config.width, config.height, 200, 200)

  preferredSize = (w + 10, h * config.instances.size)
  focusable = true
  background = Color.GRAY

  config.instances foreach { _ =>
    contents += new ImgPanel(w, h)
  }

}
