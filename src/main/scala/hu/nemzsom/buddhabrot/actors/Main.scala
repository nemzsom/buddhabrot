package hu.nemzsom.buddhabrot.actors

import akka.actor._
import java.awt.image.BufferedImage
import scala.swing.Label
import hu.nemzsom.buddhabrot.gui.{ImgPanel, InstancePanel}
import hu.nemzsom.buddhabrot.{Grid, App}
import hu.nemzsom.buddhabrot.util.ImageSaver

class Main(imgPanel: ImgPanel, instancePanel: InstancePanel, msgLabel: Label) extends Actor with ActorLogging {

  import App.config

  val display = context.actorOf(Props(new Display(imgPanel)), "display")

  override def receive = nextInstance(List())

  def nextInstance(grids: List[Grid]): Receive = {
    val index = grids.size
    if (config.instances.size == index) {
      display ! UpdateMainMessage("")
      display ! UpdateSecMessage("Building image...")
      val time = System.nanoTime
      val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB) // TODO val img = ImageBuilder.build(grids.reverse.zip(config.instances))
      log.debug(s"Image build time: ${"%.2f" format ((System.nanoTime() - time) / 1E6)} ms")
      display ! UpdateSecMessage("Saving image...")
      val savedImg = new ImageSaver(config.outRoot).saveImage(img)
      display ! UpdateSecMessage(s"Image saved to $savedImg.")
      context.stop(self)
      Actor.emptyBehavior
    }
    else {
      val instance = config.instances(index)
      display ! UpdateMainMessage(s"Calculating image ${index + 1}/${config.instances.size} - maxIter: ${instance.maxIter}")
      val coordinator = context.actorOf(Props(classOf[Coordinator], self, display, instance), s"Coordinator-${index + 1}")
      calculation(grids, coordinator)
    }
  }

  def calculation(grids: List[Grid], coordinator: ActorRef): Receive = {
    case Result(grid) =>
      context.stop(coordinator)
      context.become(nextInstance(grid :: grids))
  }

  
}
