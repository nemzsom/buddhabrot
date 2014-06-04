package hu.nemzsom.buddhabrot.gui

import javax.swing.Icon
import java.awt.{Color, Graphics, Component}
import java.awt.image.BufferedImage
import hu.nemzsom.buddhabrot.ColorScheme
import hu.nemzsom.buddhabrot.util.ImageUtil.BufferedImageOps

class ColorSchemeIcon(width: Int, height: Int) extends Icon {

  private var _scheme: ColorScheme = _
  private val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  private val ratio = 1.0 / (width - 1)

  scheme = ColorScheme(Color.BLACK, 0.0)

  def scheme = _scheme

  def scheme_=(sch: ColorScheme) = {
    _scheme = sch
    val pixels = img.pixels
    for {
      x <- 0 to width - 1
      color = sch.color(x * ratio)
      y <- 0 to height - 1
    } {
      pixels(y * width + x) = color.getRGB
    }
  }

  override def paintIcon(c: Component, g: Graphics, x: Int, y: Int): Unit = {
    g.drawImage(img, 0, 0, img.getWidth, img.getHeight, null)
  }

  override def getIconHeight: Int = height

  override def getIconWidth: Int = width
}
