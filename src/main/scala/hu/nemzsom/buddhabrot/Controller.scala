package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import scala.concurrent.duration._

class Controller(display: ActorRef) extends Actor with ActorLogging {

  import Main.config

  val grid = new Grid(config.width, config.height, config.reFrom, config.reTo, config.imFrom, config.imTo)
  val calc = context.actorOf(Props(classOf[Calculator], config))

  calc ! Calculate
  import context._
  context.system.scheduler.schedule(1000 millis, 100 millis, self, "tick")

  override def receive = {
    case Path(seq) =>
      seq foreach grid.register
      calc ! Calculate
    case "tick" => display ! Update(grid)
  }
}
