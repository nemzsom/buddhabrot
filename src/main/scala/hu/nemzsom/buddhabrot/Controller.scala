package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import scala.concurrent.duration._

case object Tick

class Controller(display: ActorRef) extends Actor with ActorLogging {

  import Main.config
  val calcCommand = Calculate(100000)

  val grid = new Grid(config.width, config.height, config.reFrom, config.reTo, config.imFrom, config.imTo)
  val stats = new Statistics(20)

  val calcs = startCalcs(Runtime.getRuntime.availableProcessors())

  import context._
  val ticker = context.system.scheduler.schedule(1000 millis, 1000 millis, self, Tick)
  var nextPreview = 5

  override def receive = {
    case Tracks(seq, sample, iter) =>
      handleTracks(seq, sample, iter)
      if (stats.sampleCount >= config.samples) {
        ticker.cancel()
        display ! UpdateMessage(s"Finishing image")
        become(toEnd(calcs - sender))
      }
      else {
        sender ! calcCommand
      }
    case Tick =>
      val iteration = stats.tick()
      val percent = stats.sampleCount * 100.0 / config.samples
      if (percent > nextPreview) {
        nextPreview = nextPreview + 5
        display ! Preview(grid)
      }
      display ! UpdateMessage(s"samples: ${"%.2f" format percent}% speed: ${iteration / 1000 / stats.ticksPerStat}K iteration/sec")
  }

  def toEnd(remained: Set[ActorRef]): Receive =
    if (remained.isEmpty) {
      display ! UpdateMessage("Saving image...")
      val time = System.nanoTime
      val img = ImageBuilder.build(grid)
      log.debug(s"Image save time: ${"%.2f" format ((System.nanoTime() - time) / 1E6)} ms")
      val savedImg = new ImageSaver(config.outDir).saveImage(img)
      display ! UpdateMessage(s"Image saved to $savedImg.")
      end
    }
    else waitForEnd(remained)

  def waitForEnd(remained: Set[ActorRef]): Receive = {
    case Tracks(seq, sample, iter) =>
      handleTracks(seq, sample, iter)
      become(toEnd(remained - sender))
  }

  def end: Receive = {
    case msg => log.error(s"message at end: $msg")
  }

  def handleTracks(points: Seq[Complex], sample: Int, iter: Int): Unit = {
    points foreach grid.register
    stats.sampleCount = stats.sampleCount + sample
    stats.iters(iter)
  }

  def startCalcs(n: Int): Set[ActorRef] = {
    val calcs = (1 to n) map { _ =>
      context.actorOf(Props(classOf[Calculator], config))
    }
    calcs.foreach(_ ! calcCommand)
    log.info(s"$n concurrent calculators started")
    calcs.toSet
  }
}
