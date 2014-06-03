package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing._
import scala.swing.BorderPanel.Position._
import ImageUtil.getTargetDimension
import scala.swing.Swing.EmptyIcon
import javax.swing.BorderFactory
import java.awt.Color
import scala.swing.ScrollPane.BarPolicy

object App extends SimpleSwingApplication {

  val config = Configs.combined
  val (imgWidth, imgHeight) = getTargetDimension(config.width, config.height, 640, 640)
  val imgPanel = new ImgPanel(imgWidth, imgHeight)
  val instancePanel = new InstancePanel
  val msgLabel = new Label("Init...", EmptyIcon, Alignment.Left) {

    border = BorderFactory.createEmptyBorder(0, 5, 2, 0)
  }
  val system = ActorSystem("Buddhabrot")
  val main = system.actorOf(Props(classOf[Main], imgPanel, instancePanel, msgLabel), "Main")

  def top = new MainFrame {
    title = s"Buddhabrot ${config.width}×${config.height} - real[${config.reFrom} - ${config.reTo}]_imag[${config.imFrom} - ${config.imTo}]"
    contents = new BorderPanel {
      background = Color.GRAY
      layout(imgPanel) = Center
      layout(new ScrollPane(instancePanel){
        verticalScrollBarPolicy = BarPolicy.Always
        horizontalScrollBarPolicy = BarPolicy.Never
        verticalScrollBar.unitIncrement = 16
      }) = West
      layout(msgLabel) = South
    }
  }
}