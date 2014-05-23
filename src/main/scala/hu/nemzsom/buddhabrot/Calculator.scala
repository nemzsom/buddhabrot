package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import scala.util.Random
import scala.annotation.tailrec

case object Calculate
case class Path(seq: Seq[Complex], iter: Int)

class Calculator(config: Config) extends Actor with ActorLogging {

  val calc = new BuddhaCalc(config.reFrom, config.reTo, config.imFrom, config.imTo)

  override def receive = {
    case Calculate =>
      val iterSeq = nextEscaped
      sender ! Path(iterSeq.seq, iterSeq.iter)
  }

  @tailrec
  private def nextEscaped: IterationSeq = {
    val seq = calc.nextSeq(config.maxIter)
    if (seq.escaped) seq
    else nextEscaped
  }
}
