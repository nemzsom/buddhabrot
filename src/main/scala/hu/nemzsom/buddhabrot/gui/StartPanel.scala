package hu.nemzsom.buddhabrot.gui

import scala.swing._
import scala.swing.BorderPanel.Position._
import javax.swing.{JLabel, BorderFactory, ImageIcon}
import scala.swing.Swing._
import scala.swing.event.{TableRowsSelected, ButtonClicked}
import hu.nemzsom.buddhabrot.Config
import scala.swing.ScrollPane.BarPolicy
import scala.swing.Table.IntervalMode
import java.nio.file.Paths
import scala.swing.event.ButtonClicked
import scala.swing.event.TableRowsSelected

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
  val table = new Table(configs.toArray map { conf =>
    Array[Any](s"${conf.width} × ${conf.height}", s"${conf.reFrom} - ${conf.reTo}", s"${conf.imFrom} - ${conf.imTo}")
  }, Seq("Size", "Real range", "Imag range")) {
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
      val dialog = new AddConfDialog
      dialog.centerOnScreen()
      dialog.open()
    case ButtonClicked(b) if b == startBtn =>
      onSelect(configs(table.selection.rows.leadIndex))
    case TableRowsSelected(_, _, false) =>
      startBtn.enabled = true
  }
}

class AddConfDialog extends Dialog {
  modal = true
  contents = new GridPanel(5, 2) {
    contents += new Label("Width:", EmptyIcon, Alignment.Left)
    contents += new TextField
    contents += new Label("Height:", EmptyIcon, Alignment.Left)
    contents += new TextField
    contents += new Label("Real from:", EmptyIcon, Alignment.Left)
    contents += new TextField
    contents += new Label("Real to:", EmptyIcon, Alignment.Left)
    contents += new TextField
    contents += new Label("Imag from:", EmptyIcon, Alignment.Left)
    contents += new TextField

    hGap = 3
    vGap = 2
  }
}
