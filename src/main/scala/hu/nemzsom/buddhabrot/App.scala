package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}
import scala.swing.{MainFrame, SimpleSwingApplication}
import java.util.concurrent.{LinkedBlockingQueue, LinkedBlockingDeque, ArrayBlockingQueue}

object App extends SimpleSwingApplication {

  val config = Configs.simple
  val panel = new Panel
  val system = ActorSystem("Buddhabrot")
  val display = system.actorOf(Props(new Display(panel)), "display")
  val coordinator = system.actorOf(Props(new Coordinator(display)), "coordinator")

  def top = new MainFrame {
    title = "Buddhabrot"
    contents = panel
  }

}