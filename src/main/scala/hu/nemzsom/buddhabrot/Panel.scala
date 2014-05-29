package hu.nemzsom.buddhabrot

import scala.swing.Swing._
import scala.swing._
import java.awt.image.BufferedImage
import java.awt.{Color, Graphics2D}
import scala.swing.BorderPanel.Position
import javax.swing.BorderFactory

class Panel extends BorderPanel {

  import App.config

  private var _img: BufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

  preferredSize = ImageBuilder.getTargetDimension(config.width, config.height, 640, 640)
  focusable = true
  background = Color.BLACK
  
  val mainMsg, secMsg = new Label("", EmptyIcon, Alignment.Left) {
    border = BorderFactory.createEmptyBorder(0, 5, 2, 0)
  }
  val msgPanel = new GridPanel(2, 1) {
    opaque = false
    contents ++= Seq(mainMsg, secMsg)
  }

  layout(msgPanel) = Position.South

  def updateImage(img: BufferedImage): Unit =
    _img = img

  def updateSecondaryMessage(msgStr: String): Unit =
    secMsg.text = msgStr
  
  def updateMainMessage(msgStr: String): Unit =
    mainMsg.text = msgStr

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    g.drawImage(_img, 0, 0, _img.getWidth, _img.getHeight, null)
  }
}