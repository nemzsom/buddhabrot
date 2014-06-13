package hu.nemzsom.buddhabrot.gui

import java.awt.{Graphics, Color}
import java.util.UUID
import javax.swing.border.BevelBorder
import javax.swing.{BorderFactory, ImageIcon}

import scala.swing.BorderPanel.Position._
import scala.swing.ScrollPane.BarPolicy
import scala.swing.event.ButtonClicked
import scala.swing._
import scala.swing.Swing._
import hu.nemzsom.buddhabrot.util.ImageUtil
import ImageUtil._
import hu.nemzsom.buddhabrot.{ColorScheme, Config, Instance}

class InstancePanel(conf: Config) extends BoxPanel(Orientation.Vertical) {

  val (w, h) = getTargetDimension(conf.width, conf.height, 200, 200)
  preferredSize = (w + 25, h * conf.instances.size)

  border = BorderFactory.createEmptyBorder(5, 5, 5, 5)

  focusable = true
  val addBtn = new Button {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("add.png"))
    tooltip = "Add new instance"
  }

  listenTo(addBtn)

  val thumbs = new BoxPanel(Orientation.Vertical) {

    var ts = List.empty[InstanceThumb]

    def newThumb(): Unit = {
      val thumb = new InstanceThumb(w, h, ColorScheme.BLACK_TO_WHITE)
      contents += VStrut(10)
      contents += thumb
      ts = thumb :: ts
      preferredSize = (w + 6, (h + 30) * conf.instances.size)
      peer.updateUI()
    }
  }

  conf.instances foreach { _ =>
    thumbs.newThumb()
  }

  // TODO remove
  thumbs.newThumb()
  thumbs.newThumb()

  contents += new BoxPanel(Orientation.Horizontal) {
    contents += addBtn
    contents += HGlue
  }
  contents += new ScrollPane(thumbs) {
    horizontalScrollBarPolicy = BarPolicy.Never
    verticalScrollBarPolicy = BarPolicy.Always
    verticalScrollBar.unitIncrement = 16
  }
  
  reactions += {
    case ButtonClicked(b) if b == addBtn =>
      val dialog = new InstanceDialog("Add Instance", instance => {
        conf.addInstance(instance)
        thumbs.newThumb()
      })
      dialog.centerOnScreen()
      dialog.open()
  }

  def setColors(show: Boolean) = {
    thumbs.ts foreach { th =>
      th.colorSchemeIndicator.visible = show
    }
  }
}

class InstanceThumb(imgWidth: Int, imgHeight: Int, initialScheme: ColorScheme) extends FlowPanel {
  
  val enabledChBox = new CheckBox
  enabledChBox.selected = true
  val colorSchemeIndicator = new ColorSchemeIndicator(imgWidth / 2, 20, Alignment.Right, initialScheme)
  val imgPanel = new ImgPanel(imgWidth, imgHeight)
  border = BorderFactory.createEtchedBorder()
  preferredSize = (imgWidth, imgHeight + 24)
  minimumSize = (imgWidth, imgHeight + 24)
  maximumSize = (imgWidth, imgHeight + 24)
  
  contents += new BoxPanel(Orientation.Horizontal) {
    preferredSize = (imgWidth, 20)
    contents += enabledChBox
    contents += HGlue
    contents += colorSchemeIndicator
    contents += HStrut(6)
  }

  contents += imgPanel
  listenTo(enabledChBox, colorSchemeIndicator)

  reactions += {
    case ButtonClicked(b) if b == enabledChBox =>
      repaint()
  }

  override def paint(g: Graphics2D) {
    super.paint(g)
    if (!enabledChBox.selected) {
      g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.9f))
      g.fillRect(0, 0, size.width, size.height)
    }

  }
}

class InstanceDialog(_title: String, onInstance: Instance => Unit) extends Dialog {

  title = _title

  modal = true
  resizable = false

  val msg = new Label("", EmptyIcon, Alignment.Left) {
    preferredSize = (240, 20)
    foreground = Color.RED
  }
  val cancelBtn = new Button("Cancel") {
    preferredSize = (68, 30)
  }
  val okBtn = new Button("Ok") {
    preferredSize = (68, 30)
  }

  val maxIterField = new TextField {
    name = "Max Iter"
  }
  val sFactorField = new TextField {
    name = "Sample Factor"
  }

  listenTo(okBtn, cancelBtn)

  contents = new BorderPanel {
    border = BorderFactory.createEmptyBorder(10, 10, 4, 10)

    layout(new GridPanel(6, 2) {
      contents += new Label(maxIterField.name + ":", EmptyIcon, Alignment.Left)
      contents += maxIterField
      contents += new Label(sFactorField.name + ":", EmptyIcon, Alignment.Left)
      contents += sFactorField
      contents += new BoxPanel(Orientation.Horizontal) {
        border = BorderFactory.createEmptyBorder(2, 2, 2, 2)
        contents += HGlue
        contents += okBtn
        contents += cancelBtn
      }
      hGap = 3
      vGap = 2
    }) = North

    layout(msg) = South
  }

  reactions += {
    case ButtonClicked(b) if b == okBtn =>
      try {
        val maxIter = maxIterField.asInt
        val sFactor = sFactorField.asInt
        onInstance(Instance(maxIter, sFactor, UUID.randomUUID))
        close()
      } catch {
        case e: InvalidInputException =>
          msg.text = e.getMessage
      }
    case ButtonClicked(b) if b == cancelBtn =>
      close()
  }

  implicit class TextFieldOps(t: TextField) {

    def asInt: Int = {
      try {
        t.text.toInt
      } catch {
        case e: NumberFormatException =>
          throw new InvalidInputException(t.name, "Int")
      }
    }
  }
}
