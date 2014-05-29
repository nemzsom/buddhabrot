package hu.nemzsom.buddhabrot

import akka.actor._
import scala.concurrent.duration._

case class Result(grid: Grid)
case object Tick

class Coordinator(main: ActorRef, display: ActorRef, instance: Instance) extends Actor with ActorLogging {

  import App.config
  val calcCommand = Calculate(100000)

  val grid = new Grid(config.width, config.height, config.reFrom, config.reTo, config.imFrom, config.imTo)
  val stats = new Statistics(20)

  val calcs = startCalcs(Runtime.getRuntime.availableProcessors())

  implicit val dispatcher = context.dispatcher
  val ticker = context.system.scheduler.schedule(1000 millis, 1000 millis, self, Tick)
  var nextPreview = 5

  override def receive = {
    case Tracks(seq, sample, iter) =>
      handleTracks(seq, sample, iter)
      if (stats.sampleCount >= instance.samples) {
        ticker.cancel()
        display ! UpdateSecMessage(s"Finishing image")
        context.become(finishing(calcs - sender))
      }
      else {
        sender ! calcCommand
      }
    case Tick =>
      val iteration = stats.tick()
      val percent = stats.sampleCount * 100.0 / instance.samples
      if (percent > nextPreview) {
        nextPreview = nextPreview + 5
        display ! Preview(grid)
      }
      display ! UpdateSecMessage(s"samples: ${"%.2f" format percent}% speed: ${iteration / 1000 / stats.ticksPerStat}K iteration/sec")
  }

  def finishing(remained: Set[ActorRef]): Receive =
    if (remained.isEmpty) {
      main ! Result(grid)
      Actor.emptyBehavior
    }
    else waitForEnd(remained)

  def waitForEnd(remained: Set[ActorRef]): Receive = {
    case Tracks(seq, sample, iter) =>
      handleTracks(seq, sample, iter)
      context.become(finishing(remained - sender))
  }

  def handleTracks(points: Seq[Complex], sample: Int, iter: Int): Unit = {
    points foreach grid.register
    stats.sampleCount = stats.sampleCount + sample
    stats.iters(iter)
  }

  def startCalcs(n: Int): Set[ActorRef] = {
    val calcs = (1 to n) map { i =>
      context.actorOf(Props(classOf[Calculator], instance.maxIter), s"Calculator-$i")
    }
    calcs.foreach(_ ! calcCommand)
    log.info(s"$n concurrent calculators started")
    calcs.toSet
  }
}
