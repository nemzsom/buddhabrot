package hu.nemzsom.buddhabrot.gui

import scala.swing._
import scala.swing.BorderPanel.Position._
import javax.swing.{BorderFactory, ImageIcon}
import scala.swing.Swing._
import scala.swing.event.{TableColumnsSelected, TableRowsSelected, ButtonClicked}
import hu.nemzsom.buddhabrot.Config
import scala.swing.ScrollPane.BarPolicy
import scala.swing.Table.IntervalMode

class StartPanel(onSelect: Config => Unit) extends BorderPanel {

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
  val table = new Table(testData, Seq("Size", "Real range", "Image range")) {
    selection.intervalMode = IntervalMode.Single
  }

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
      println(s"new config")
    case ButtonClicked(b) if b == startBtn =>
      println(s"start calculation")
    case TableRowsSelected(source, range, false) =>
      outputSelection(source, "Rows selected, changes: %s" format range)
    case TableColumnsSelected(source, range, false) =>
      outputSelection(source, "Columns selected, changes: %s" format range)
    case e => println("%s => %s" format(e.getClass.getSimpleName, e.toString))
  }

  def outputSelection(table: Table, msg: String) {
    val rowId = table.selection.rows.leadIndex
    val colId = table.selection.columns.leadIndex
    val rows = table.selection.rows.mkString(", ")
    val cols = table.selection.columns.mkString(", ")
    println("%s\n  Lead: %s, %s; Rows: %s; Columns: %s\n" format (msg, rowId, colId, rows, cols))
  }
}
