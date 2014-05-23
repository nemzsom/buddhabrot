package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import scala.concurrent.duration._

case object Tick

class Controller(display: ActorRef) extends Actor {

  import Main.config

  val grid = new Grid(config.width, config.height, config.reFrom, config.reTo, config.imFrom, config.imTo)
  val stats = new Statistics(20)

  val calcs = startCalcs

  import context._
  context.system.scheduler.schedule(100 millis, 100 millis, display, UpdatePixels(grid))
  context.system.scheduler.schedule(1000 millis, 1000 millis, self, Tick)

  override def receive = {
    case Path(seq, iter) =>
      seq foreach grid.register
      stats.pathCount = stats.pathCount + 1
      stats.iters(iter)
      sender ! Calculate
    case Tick =>
      val iteration = stats.tick()
      display ! UpdateMessage(s"paths: ${stats.pathCount / 1000000}M speed: ${iteration / 1000 / stats.ticksPerStat}K iteration/sec")
  }

  def startCalcs: Seq[ActorRef] = {
    val calcs = (0 to Runtime.getRuntime.availableProcessors()) map { _ =>
      context.actorOf(Props(classOf[Calculator], config))
    }
    calcs.foreach(_ ! Calculate)
    calcs
  }
}
