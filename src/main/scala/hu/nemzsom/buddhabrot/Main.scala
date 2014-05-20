package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing.{MainFrame, SimpleSwingApplication}

object Main extends SimpleSwingApplication {

  val config = Config.default
  val panel = new ImagePanel(640, 480)
  val system = ActorSystem("Buddhabrot")
  val display = system.actorOf(Props(new Display(panel)), "display")
  val controller = system.actorOf(Props(new Controller(display)), "controller")


  def top = new MainFrame {
    title = "Buddhabrot"
    contents = panel
  }

}
