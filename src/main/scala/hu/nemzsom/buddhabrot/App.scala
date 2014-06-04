package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing._
import scala.swing.BorderPanel.Position._
import hu.nemzsom.buddhabrot.util.ImageUtil
import ImageUtil.getTargetDimension
import scala.swing.Swing.EmptyIcon
import javax.swing.BorderFactory
import java.awt.Color
import scala.swing.ScrollPane.BarPolicy
import hu.nemzsom.buddhabrot.gui.{StartPanel, ImgPanel, InstancePanel}
import hu.nemzsom.buddhabrot.actors.Main

object App extends SimpleSwingApplication {

  setSystemLookAndFeel()

  val config = Configs.combined
  val (imgWidth, imgHeight) = getTargetDimension(config.width, config.height, 640, 640)
  val imgPanel = new ImgPanel(imgWidth, imgHeight)
  val instancePanel = new InstancePanel
  val msgLabel = new Label("Init...", EmptyIcon, Alignment.Left) {

    border = BorderFactory.createEmptyBorder(0, 5, 2, 0)
  }
  val system = ActorSystem("Buddhabrot")
  val main = system.actorOf(Props(classOf[Main], imgPanel, instancePanel, msgLabel), "Main")

  val mainGui = new BorderPanel {
    background = Color.GRAY
    layout(imgPanel) = Center
    layout(new ScrollPane(instancePanel){
      horizontalScrollBarPolicy = BarPolicy.Never
      verticalScrollBar.unitIncrement = 16
    }) = West
    layout(msgLabel) = South
  }

  def top = new MainFrame {
    title = "Buddhabrot generator"
    resizable = false
    contents = new StartPanel({ conf =>
      println("config selected")
      contents = mainGui
      resizable = true
      centerOnScreen()
    })
    centerOnScreen()
  }

  def setSystemLookAndFeel() {
    import javax.swing.UIManager
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  }
}