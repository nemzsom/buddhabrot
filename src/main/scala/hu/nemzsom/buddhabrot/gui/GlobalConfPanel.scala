package hu.nemzsom.buddhabrot.gui

import scala.swing._
import scala.swing.Swing._
import hu.nemzsom.buddhabrot.{Coloring, Config}
import javax.swing.BorderFactory
import java.awt.Color

class GlobalConfPanel(conf: Config) extends BoxPanel(Orientation.Vertical) {

  border = BorderFactory.createEmptyBorder(5, 5, 3, 5)

  // color
  val individualColor = radioButton("individual")
  val globalColor = radioButton("global")
  val colorGroup = new ButtonGroup(individualColor, globalColor)
  val globalColorIcon = new ColorSchemeIcon(200, 20)
  val globalColorScheme = new Label("", globalColorIcon, Alignment.Left) {
    border = BorderFactory.createLineBorder(Color.BLACK)
  }
  colorGroup.select(conf.coloring match {
    case Coloring.Individual => individualColor
    case Coloring.Global => globalColor
  })
  globalColorScheme.visible = conf.coloring == Coloring.Global
  globalColorIcon.scheme = conf.globalColorScheme
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("color")
    contents += individualColor
    contents += globalColor
    contents += globalColorScheme
  }

  // color mixing
  val additiveMix = radioButton("additive")
  val averageMix = radioButton("average")
  val mixGroup = new ButtonGroup(additiveMix, averageMix)
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("color mixing")
    contents += additiveMix
    contents += averageMix
  }

  // preview
  val individualPreview = radioButton("individual")
  val mixPreview = radioButton("mix")
  val previewGroup = new ButtonGroup(individualPreview, mixPreview)
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("preview")
    contents += individualPreview
    contents += mixPreview
  }

  def radioButton(name: String) = new RadioButton(name)  {
    preferredSize = (100, 16)
  }

  def label(name: String) = new Label(s"$name:", EmptyIcon, Alignment.Left) {
    preferredSize = (100, 16)
  }


}
