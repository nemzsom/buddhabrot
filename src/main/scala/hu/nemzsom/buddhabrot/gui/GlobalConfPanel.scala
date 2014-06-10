package hu.nemzsom.buddhabrot.gui

import scala.swing._
import scala.swing.Swing._
import hu.nemzsom.buddhabrot.{Coloring, ColorMixing, Config}
import javax.swing.BorderFactory
import scala.swing.event.ButtonClicked

class GlobalConfPanel(conf: Config) extends BoxPanel(Orientation.Vertical) {

  border = BorderFactory.createEmptyBorder(5, 5, 3, 5)

  // color
  val individualColor = radioButton("individual", 22)
  val globalColor = radioButton("global", 22)
  val colorGroup = new ButtonGroup(individualColor, globalColor)
  val globalColorSchemeChooser = new ColorSchemeChooser(200, 20, Alignment.Left, conf.globalColorScheme)
  colorGroup.select(conf.coloring match {
    case Coloring.Individual => individualColor
    case Coloring.Global => globalColor
  })
  globalColorSchemeChooser.visible = conf.coloring == Coloring.Global
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("color", 22)
    contents += individualColor
    contents += globalColor
    contents += globalColorSchemeChooser
  }
  listenTo(individualColor, globalColor)

  reactions += {
    case ButtonClicked(b) if b == individualColor => globalColorSchemeChooser.visible = false
    case ButtonClicked(b) if b == globalColor => globalColorSchemeChooser.visible = true
  }

  // color mixing
  val additiveMix = radioButton("additive", 16)
  val averageMix = radioButton("average", 16)
  val mixGroup = new ButtonGroup(additiveMix, averageMix)
  mixGroup.select(conf.colorMixing match {
    case ColorMixing.Additive => additiveMix
    case ColorMixing.Average => averageMix
  })
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("color mixing", 16)
    contents += additiveMix
    contents += averageMix
  }

  // preview
  val individualPreview = radioButton("individual", 16)
  val mixPreview = radioButton("mix", 16)
  val previewGroup = new ButtonGroup(individualPreview, mixPreview)
  previewGroup select mixPreview
  contents += new FlowPanel(FlowPanel.Alignment.Leading)() {
    contents += label("preview", 16)
    contents += individualPreview
    contents += mixPreview
  }

  def radioButton(name: String, height: Int) = new RadioButton(name)  {
    preferredSize = (100, height)
  }

  def label(name: String,  height: Int) = new Label(s"$name:", EmptyIcon, Alignment.Left) {
    preferredSize = (100, height)
  }


}
