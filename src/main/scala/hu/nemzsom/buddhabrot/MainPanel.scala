package hu.nemzsom.buddhabrot

import scala.swing.Swing._
import scala.swing._
import java.awt.image.{DataBufferInt, BufferedImage}
import java.awt.Graphics2D
import scala.swing.BorderPanel.Position
import javax.swing.BorderFactory
import scala.swing.event._

class MainPanel(initialWidth: Int, initialHeight: Int) extends BorderPanel {

  preferredSize = (initialWidth, initialHeight)
  focusable = true
  val msg = new Label("", EmptyIcon, Alignment.Left)
  msg.peer.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0))
  layout(msg) = Position.South

  val img = new BufferedImage(initialWidth, initialHeight, BufferedImage.TYPE_INT_RGB)

  def data: Array[Int] = {
    val raster = img.getRaster
    val databuffer: DataBufferInt = raster.getDataBuffer.asInstanceOf[DataBufferInt]
    databuffer.getData
  }

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    g.drawImage(img, 0, 0, img.getWidth, img.getHeight, null)
  }
}