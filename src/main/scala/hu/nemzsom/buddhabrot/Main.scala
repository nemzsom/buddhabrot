package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing.{MainFrame, SimpleSwingApplication}
import java.util.concurrent.{LinkedBlockingQueue, LinkedBlockingDeque, ArrayBlockingQueue}

object Main extends SimpleSwingApplication {

  val config = Config.default
  val panel = new MainPanel(config.width, config.height)
  val system = ActorSystem("Buddhabrot")
  val display = system.actorOf(Props(new Display(panel)), "display")
  val controller = system.actorOf(Props(new Controller(display)), "controller")

  def top = new MainFrame {
    title = "Buddhabrot"
    contents = panel
  }

}