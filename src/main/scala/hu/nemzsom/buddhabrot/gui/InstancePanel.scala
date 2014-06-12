package hu.nemzsom.buddhabrot.gui

import java.awt.Color
import javax.swing.{BorderFactory, ImageIcon}

import scala.swing.BorderPanel.Position._
import scala.swing.ScrollPane.BarPolicy
import scala.swing.event.ButtonClicked
import scala.swing._
import scala.swing.Swing._
import hu.nemzsom.buddhabrot.util.ImageUtil
import ImageUtil._
import hu.nemzsom.buddhabrot.{Config, Instance}

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

  val images = new BoxPanel(Orientation.Vertical) {

    def newImage(): Unit = {
      contents += VStrut(10)
      contents += new ImgPanel(w, h)
      preferredSize = (w, h * conf.instances.size)
      peer.updateUI()
    }
  }
  conf.instances foreach { _ =>
    images.newImage
  }

  contents += new BoxPanel(Orientation.Horizontal) {
    contents += addBtn
    contents += HGlue
  }
  contents += new ScrollPane(images){
    horizontalScrollBarPolicy = BarPolicy.Never
    verticalScrollBarPolicy = BarPolicy.Always
    verticalScrollBar.unitIncrement = 16
  }
  
  reactions += {
    case ButtonClicked(b) if b == addBtn =>
      val dialog = new InstanceDialog("Add Instance", instance => {
        conf.addInstance(instance)
        println(s"added new instance ${conf.instances}")
        images.newImage()
      })
      dialog.centerOnScreen()
      dialog.open()
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
        onInstance(Instance(maxIter, sFactor))
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
