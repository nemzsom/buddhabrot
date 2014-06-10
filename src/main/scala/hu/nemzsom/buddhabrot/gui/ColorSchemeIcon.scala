package hu.nemzsom.buddhabrot.gui

import javax.swing.{ImageIcon, BorderFactory, Icon}
import java.awt.{Color, Graphics, Component}
import java.awt.image.BufferedImage
import hu.nemzsom.buddhabrot.ColorScheme
import hu.nemzsom.buddhabrot.util.ImageUtil.BufferedImageOps
import scala.swing._
import scala.swing.Swing._
import scala.swing.event.{ValueChanged, ButtonClicked, MouseClicked}
import hu.nemzsom.buddhabrot.ColorFlag

class ColorSchemeChooser(width: Int, height: Int,  align: Alignment.Value, initialScheme: ColorScheme) extends Label("", EmptyIcon, align) {

  val scheme = initialScheme

  icon = new ColorSchemeIcon(width, height, scheme)
  border = BorderFactory.createLineBorder(Color.BLACK)
  listenTo(mouse.clicks)

  reactions += {
    case e: MouseClicked =>
      val chooser = new ChooserDialog(scheme)
      chooser.centerOnScreen()
      chooser.open()
  }
}

class ChooserDialog(initialScheme: ColorScheme) extends Dialog {

  resizable = false
  modal = true

  val add = new Button("") {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("add.png"))
    tooltip = "Add new color"
  }

  val icon = new ColorSchemeIcon(200, 30, initialScheme)

  val gridPanel = new BoxPanel(Orientation.Vertical) {

    contents += new FlowPanel {
      contents += new Label {
        preferredSize = (30, 30)
      }
      contents += new Label("", icon, Alignment.Center)
      contents += new Label {
        preferredSize = (30, 30)
      }
    }

    initialScheme.flags foreach { flag =>
      contents += new ColorFlagPanel(flag)
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += add
      contents += HGlue
    }

  }

  contents = gridPanel

  listenTo(add)

  reactions += {
    case ButtonClicked(b) =>
      pack()
      repaint()
  }
}

class ColorChooserBox(color: Color) extends Label {
  opaque = true
  preferredSize = (30, 30)
  background = color
}

class LocSlider(initialValue: Double) extends Slider {
  min = 0
  max = Int.MaxValue
  value = (initialValue * max).toInt
  
  def doubleValue = value.toDouble / Int.MaxValue
}

class ColorFlagPanel(flag: ColorFlag) extends FlowPanel {

  val slider =  new LocSlider(flag.loc) {
    preferredSize = (200, 30)
  }

  contents += new ColorChooserBox(flag.color)
  contents += slider
  contents += new Button("") {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("remove.png"))
    tooltip = "Remove color"
  }

  listenTo(slider)

  reactions += {
    case e: ValueChanged => println(s"value changed: ${slider.doubleValue}\n$e")
  }
}

class ColorSchemeIcon(width: Int, height: Int, initialScheme: ColorScheme) extends Icon {

  private var _scheme: ColorScheme = _
  private val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  private val ratio = 1.0 / (width - 1)

  scheme = initialScheme

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

object ColorChooserTester extends SimpleSwingApplication {

  override def top = new MainFrame {
    title = "ColorChooserTester"
    contents = new ColorSchemeChooser(200, 20, Alignment.Center, ColorScheme.BLACK_TO_WHITE)
    centerOnScreen()
  }
}


