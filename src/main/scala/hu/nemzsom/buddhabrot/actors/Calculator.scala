package hu.nemzsom.buddhabrot.actors

import akka.actor.{ActorLogging, Actor}
import scala.annotation.tailrec
import hu.nemzsom.buddhabrot.{Complex, BuddhaCalc, App}

/** Receive: calculate tracks for iterations */
case class Calculate(iterations: Int)

/** Response: calculated tracks of complex points during the mandelbrot iteration.
  *
  * @param points points of the iterations that escaped
  * @param sample how many samples have taken
  * @param allIter hom many iteration step performed (including the non-escaped points)
  */
case class Tracks(points: Seq[Complex], sample: Int, allIter: Int)

class Calculator(maxIter: Int) extends Actor with ActorLogging {

  import App.config

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
        val nextSeq = calc.nextSeq(maxIter)
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
