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
import hu.nemzsom.buddhabrot.gui._
import hu.nemzsom.buddhabrot.actors.Main

object App extends SimpleSwingApplication {

  setSystemLookAndFeel()

  def top = new MainFrame {
    title = "Buddhabrot generator"
    resizable = false
    contents = new StartPanel(Config.defOutDir, { conf =>
      contents = start(conf)
      resizable = true
      centerOnScreen()
      repaint()
    })
    centerOnScreen()
  }

  def start(conf: Config): Component = {
    val (imgWidth, imgHeight) = getTargetDimension(conf.width, conf.height, 640, 640)
    val imgPanel = new ImgPanel(imgWidth, imgHeight)
    val instancePanel: InstancePanel = new InstancePanel(conf)
    val msgLabel = new Label("Init...", EmptyIcon, Alignment.Left) {
      border = BorderFactory.createEmptyBorder(0, 5, 2, 0)
    }
    val topPanel = new GlobalConfPanel(conf)
    val system = ActorSystem("Buddhabrot")
    val main = system.actorOf(Props(classOf[Main], conf, imgPanel, instancePanel, msgLabel), "Main")

    listenTo(topPanel)

    reactions += {
      case ColoringChanged(coloring) =>
        coloring match {
          case Coloring.Global =>
            instancePanel.setColors(false)
          case Coloring.Individual =>
            instancePanel.setColors(true)
        }
    }

    new BorderPanel {
      layout(topPanel) = North
      layout(imgPanel) = Center
      layout(instancePanel) = West
      layout(msgLabel) = South
    }
  }

  def setSystemLookAndFeel() {
    import javax.swing.UIManager
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    UIManager.put("Slider.paintValue", false)
  }
}