package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing.{MainFrame, SimpleSwingApplication}

object App extends SimpleSwingApplication {

  val config = Configs.nebulaBrot
  val panel = new Panel
  val system = ActorSystem("Buddhabrot")
  val main = system.actorOf(Props(classOf[Main], panel), "Main")

  def top = new MainFrame {
    title = s"Buddhabrot ${config.width}×${config.height}"
    contents = panel
  }
}