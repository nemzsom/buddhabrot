package hu.nemzsom.buddhabrot.gui

import javax.swing.{ImageIcon, BorderFactory, Icon}
import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage
import hu.nemzsom.buddhabrot.ColorScheme
import hu.nemzsom.buddhabrot.util.ImageUtil.BufferedImageOps
import scala.swing._
import scala.swing.Swing._
import scala.swing.event._
import scala.swing.event.ButtonClicked
import hu.nemzsom.buddhabrot.ColorFlag
import scala.swing.event.MouseClicked
import java.util.UUID

class ColorSchemeIndicator(width: Int, height: Int,  align: Alignment.Value, initialScheme: ColorScheme) extends Label("", EmptyIcon, align) {

  var scheme = initialScheme

  icon = new ColorSchemeIcon(width, height, scheme)
  border = BorderFactory.createLineBorder(Color.BLACK)
  listenTo(mouse.clicks)

  var chooser: Option[ColorSchemeChooser] = None

  reactions += {
    case e: MouseClicked => chooser match {
      case None =>
        val ch = new ColorSchemeChooser(scheme) {
          title = "Choose a color"
        }
        chooser = Some(ch)
        ch.setLocationRelativeTo(this)
        ch.open()
        listenTo(ch)
      case Some(ch) =>
        ch.open()
    }
    case e: WindowClosed =>
      deafTo(e.source)
      chooser = None
    case SchemeChanged(ch) =>
      scheme = ch.scheme
      icon.asInstanceOf[ColorSchemeIcon].scheme = scheme
      repaint()
      publish(new ValueChanged(this))
  }
}

class ColorSchemeChooser(initialScheme: ColorScheme) extends Dialog {

  resizable = false

  var _scheme = initialScheme
  def scheme = _scheme
  def scheme_=(s: ColorScheme): Unit = {
    _scheme = s
    icon.scheme = s
  }

  var flagPanels = scheme.flags map (new ColorFlagPanel(_))
  flagPanels foreach(listenTo(_))

  val add = new Button("") {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("add.png"))
    tooltip = "Add new color"
  }

  val icon = new ColorSchemeIcon(200, 30, scheme)
  val schemeLabel = new Label("", icon, Alignment.Center)

  val flagHolder = new BoxPanel(Orientation.Vertical) {
    flagPanels foreach { panel =>
      contents += panel
    }
  }

  val gridPanel = new BoxPanel(Orientation.Vertical) {

    contents += new FlowPanel {
      contents += new Label {
        preferredSize = (30, 30)
      }
      contents += schemeLabel
      contents += new Label {
        preferredSize = (30, 30)
      }
    }

    contents += flagHolder

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += add
      contents += HGlue
    }
  }

  contents = gridPanel

  listenTo(add)

  reactions += {
    case ButtonClicked(b) if b == add =>
      val newFlag = ColorFlag(Color.BLACK, 0.0, UUID.randomUUID)
      val newPanel = new ColorFlagPanel(newFlag)
      scheme = scheme.add(newFlag)
      flagHolder.contents += newPanel
      flagHolder.contents foreach { flagPanel =>
        flagPanel.asInstanceOf[ColorFlagPanel].removeBtn.enabled = true
      }
      listenTo(newPanel)
      pack()
      publish(SchemeChanged(this))
    case FlagChanged(panel) =>
      scheme = scheme.updated(panel.flagId, panel.color, panel.loc)
      schemeLabel.repaint()
      publish(SchemeChanged(this))
    case FlagRemoved(panel) =>
      scheme = scheme.remove(panel.flagId)
      flagHolder.contents -= panel
      deafTo(panel)
      if (flagHolder.contents.size == 1) {
        flagHolder.contents.head.asInstanceOf[ColorFlagPanel].removeBtn.enabled = false
      }
      pack()
      publish(SchemeChanged(this))
  }

  override def closeOperation(): Unit = {
    publish(new WindowClosed(this))
  }
}

case class SchemeChanged(override val source: ColorSchemeChooser) extends UIEvent

class ColorIndicator(_color: Color) extends Label {

  var color = _color

  opaque = true
  preferredSize = (30, 30)
  background = color

  listenTo(mouse.clicks)

  reactions += {
    case e: MousePressed =>
      ColorChooser.showDialog(this, "Pick a color", color) match {
        case Some(c) =>
          color = c
          background = color
          publish(new ValueChanged(this))
        case None =>
      }
  }

}

class LocSlider(initialValue: Double) extends Slider {
  min = 0
  max = Int.MaxValue
  value = (initialValue * max).toInt
  paintLabels = false
  paintTicks = false
  paintTrack = true
  
  def doubleValue = value.toDouble / Int.MaxValue
}

class ColorFlagPanel(flag: ColorFlag) extends FlowPanel {

  val flagId = flag.id
  var color = flag.color
  var loc = flag.loc

  val slider =  new LocSlider(loc) {
    preferredSize = (200, 30)
  }

  val colorIndicator = new ColorIndicator(color)

  val removeBtn = new Button("") {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("remove.png"))
    tooltip = "Remove color"
  }

  contents += colorIndicator
  contents += slider
  contents += removeBtn

  listenTo(colorIndicator, slider, removeBtn)

  reactions += {
    case e: ValueChanged =>
      color = colorIndicator.color
      loc = slider.doubleValue
      publish(FlagChanged(this))
    case ButtonClicked(b) if b == removeBtn =>
      publish(FlagRemoved(this))
  }
}

case class FlagChanged(override val source: ColorFlagPanel) extends ComponentEvent
case class FlagRemoved(override val source: ColorFlagPanel) extends ComponentEvent

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

  override def paintIcon(c: java.awt.Component, g: Graphics, x: Int, y: Int): Unit = {
    g.drawImage(img, 0, 0, img.getWidth, img.getHeight, null)
  }

  override def getIconHeight: Int = height

  override def getIconWidth: Int = width
}

object ColorChooserTester extends SimpleSwingApplication {

  setSystemLookAndFeel()

  override def top = new MainFrame {
    title = "ColorChooserTester"
    val indicator = new ColorSchemeIndicator(200, 20, Alignment.Center, ColorScheme.BLACK_TO_WHITE)
    contents = indicator
    listenTo(indicator)
    centerOnScreen()

    reactions += {
      case e: ValueChanged =>
        println(s"value changed: ${indicator.scheme}")
    }
  }

  def setSystemLookAndFeel() {
    import javax.swing.UIManager
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    UIManager.put("Slider.paintValue", false)
  }
}


