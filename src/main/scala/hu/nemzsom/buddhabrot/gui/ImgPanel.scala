package hu.nemzsom.buddhabrot.gui

import scala.swing.Swing._
import scala.swing._
import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D}
import scala.swing.BorderPanel.Position
import javax.swing.BorderFactory

class ImgPanel(width: Int, height: Int) extends BorderPanel {

  private var _img: BufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

  preferredSize = (width, height)
  focusable = true
  background = Color.BLACK
  
  val msg = new Label("", EmptyIcon, Alignment.Left) {
    border = BorderFactory.createEmptyBorder(0, 5, 2, 0)
  }
  val msgPanel = new GridPanel(2, 1) {
    opaque = false
    contents += msg
  }

  layout(msgPanel) = Position.South

  def updateImage(img: BufferedImage): Unit =
    _img = img

  def updateMessage(msgStr: String): Unit =
    msg.text = msgStr

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    g.drawImage(_img, 0, 0, _img.getWidth, _img.getHeight, null)
  }
}