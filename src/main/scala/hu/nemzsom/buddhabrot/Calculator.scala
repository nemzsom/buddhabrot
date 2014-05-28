package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import scala.util.Random
import scala.annotation.tailrec

case class Calculate(iterations: Int)
case class Tracks(points: Seq[Complex], sample: Int, allIter: Int)

class Calculator(config: Config) extends Actor with ActorLogging {

  val calc = new BuddhaCalc(config.reFrom, config.reTo, config.imFrom, config.imTo)

  override def receive = {
    case Calculate(iterations) =>
      sender ! calculate(iterations)
  }

  private def calculate(iterations: Int): Tracks = {
    @tailrec
    def iterate(points: Seq[Complex], sample: Int, allIter: Int): Tracks = {
      if (allIter >= iterations) Tracks(points, sample, allIter)
      else {
        val nextSeq = calc.nextSeq(200) //TODO val nextSeq = calc.nextSeq(config.maxIter)
        val nextIter = allIter + nextSeq.iter
        val nextPoints = {
          if (nextSeq.escaped) nextSeq.seq ++ points
          else points
        }
        iterate(nextPoints, sample + 1, nextIter)
      }
    }
    iterate(List(), 0, 0)
  }
}
