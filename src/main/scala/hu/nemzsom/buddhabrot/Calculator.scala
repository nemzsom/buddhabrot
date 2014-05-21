package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import scala.util.Random
import scala.annotation.tailrec

case object Calculate
case class Path(seq: Seq[Complex])

class Calculator(config: Config) extends Actor with ActorLogging {

  val calc = new BuddhaCalc(config.reFrom, config.reTo, config.imFrom, config.imTo)

  override def receive = {
    case Calculate => sender ! Path(nextEscaped)
  }

  @tailrec
  private def nextEscaped: Seq[Complex]=
    calc.nextSeq(config.maxIter) match {
      case Escaped(seq) => seq
      case Stayed(_) => nextEscaped
    }
}
