package hu.nemzsom.buddhabrot

import scala.swing.Swing._
import scala.swing._
import java.awt.image.{DataBufferInt, BufferedImage}
import java.awt.{Color, Graphics2D}
import scala.swing.BorderPanel.Position
import javax.swing.BorderFactory
import scala.swing.event._

class MainPanel extends BorderPanel {

  private var _img: BufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
  // TODO scale from config
  preferredSize = (640, 480)
  focusable = true
  val msg = new Label("", EmptyIcon, Alignment.Left)
  msg.peer.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0))
  layout(msg) = Position.South
  peer.setBackground(Color.GRAY)

  def updateImage(img: BufferedImage): Unit =
    _img = img

  def updateMessage(msgStr: String): Unit =
    msg.text = msgStr

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    g.drawImage(_img, 0, 0, _img.getWidth, _img.getHeight, null)
  }
}