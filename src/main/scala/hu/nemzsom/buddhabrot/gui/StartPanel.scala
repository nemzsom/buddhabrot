package hu.nemzsom.buddhabrot.gui

import scala.swing._
import scala.swing.BorderPanel.Position._
import javax.swing.{BorderFactory, ImageIcon}
import scala.swing.Swing._
import hu.nemzsom.buddhabrot.Config
import scala.swing.ScrollPane.BarPolicy
import scala.swing.Table.IntervalMode
import java.nio.file.{Files, Paths}
import scala.swing.event.ButtonClicked
import scala.swing.event.TableRowsSelected
import javax.swing.table.AbstractTableModel
import java.awt.Color

class StartPanel(rootDir: String, onSelect: Config => Unit) extends BorderPanel {

  var configs: List[Config] = Config.loadFrom(Paths.get(Config.defOutDir)).sorted

  val newConfigBtn = new Button {
    preferredSize = (30, 30)
    icon = new ImageIcon(getClass.getResource("add.png"))
    tooltip = "Add new configuration"
  }
  val startBtn = new Button("Start") {
    enabled = false
  }

  val testData = (1 to 10).toArray map { i =>
    Array[Any](s"${i}000 × ${i}000", "-2.0 - 2.0", "-1.5 - 1.5")
  }

  val tableColumnNames = Seq("Size", "Real range", "Imag range")
  val table = new Table {
    selection.intervalMode = IntervalMode.Single
  }
  table.updateContent()

  layout(new BoxPanel(Orientation.Horizontal) {
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    contents += new Label("Configs:")
    contents += HGlue
    contents += newConfigBtn

  }) = North
  layout(new ScrollPane(table){
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    horizontalScrollBarPolicy = BarPolicy.Never
    verticalScrollBar.unitIncrement = 16
  }) = Center
  layout(new BoxPanel(Orientation.Horizontal) {
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    contents += HGlue
    contents += startBtn

  }) = South

  listenTo(newConfigBtn, startBtn, table.selection)

  reactions += {
    case ButtonClicked(b) if b == newConfigBtn =>
      val dialog = new AddConfDialog(newConf)
      dialog.centerOnScreen()
      dialog.open()
    case ButtonClicked(b) if b == startBtn =>
      onSelect(configs(table.selection.rows.leadIndex))
    case TableRowsSelected(_, _, false) =>
      startBtn.enabled = true
  }

  def newConf(conf: Config): Unit = {
    Files.createDirectories(Paths.get(conf.outDir))
    configs = (conf :: configs).sorted
    table.updateContent()
  }

  implicit class TableOps(t: Table) {

    def updateContent(): Unit = {
      val rowData = configs.toArray map { conf =>
        Array[Any](s"${conf.width} × ${conf.height}", s"${conf.reFrom} - ${conf.reTo}", s"${conf.imFrom} - ${conf.imTo}")
      }
      t.model = new AbstractTableModel {
        override def getColumnName(column: Int) = tableColumnNames(column)
        def getRowCount = rowData.length
        def getColumnCount = tableColumnNames.length
        def getValueAt(row: Int, col: Int): AnyRef = rowData(row)(col).asInstanceOf[AnyRef]
        override def isCellEditable(row: Int, column: Int) = true
        override def setValueAt(value: Any, row: Int, col: Int) {
          rowData(row)(col) = value
          fireTableCellUpdated(row, col)
        }
      }
    }
  }
}

class AddConfDialog(newConf: Config => Unit) extends Dialog {
  title = "New Config"
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

  val widthField = new TextField {
    name = "Width"
  }
  val heightField = new TextField {
    name = "Height"
  }
  val imFromField = new TextField {
    name = "Imag from"
  }
  val imToField = new TextField {
    name = "Imag to"
  }
  val rFromField = new TextField {
    name = "Real from"
  }
  
  listenTo(okBtn, cancelBtn)
  
  contents = new BorderPanel {
    border = BorderFactory.createEmptyBorder(10, 10, 4, 10)

    layout(new GridPanel(6, 2) {
      contents += new Label("Width:", EmptyIcon, Alignment.Left)
      contents += widthField
      contents += new Label("Height:", EmptyIcon, Alignment.Left)
      contents += heightField
      contents += new Label("Imag from:", EmptyIcon, Alignment.Left)
      contents += imFromField
      contents += new Label("Imag to:", EmptyIcon, Alignment.Left)
      contents += imToField
      contents += new Label("Real from:", EmptyIcon, Alignment.Left)
      contents += rFromField
      contents += new Label("")
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
        val width = widthField.asInt
        val height = heightField.asInt
        val imFrom = imFromField.asDouble
        val imTo = imToField.asDouble
        val reFrom = rFromField.asDouble
        val conf = new Config(width, height, imFrom, imTo, reFrom, Config.defOutDir)
        newConf(conf)
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

    def asDouble: Double = {
      try {
        t.text.toDouble
      } catch {
        case e: NumberFormatException =>
          throw new InvalidInputException(t.name, "Double")
      }
    }
  }
}

class InvalidInputException(fieldName: String, fieldType: String) extends Exception(s"$fieldName must be $fieldType")
