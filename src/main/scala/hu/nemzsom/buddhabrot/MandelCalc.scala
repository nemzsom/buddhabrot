package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import scala.util.Random

case object Calculate

class MandelCalc(conf: Config) extends Actor with ActorLogging {

  val rnd = new Random
  
  self ! Calculate // starts the calculation

  override def receive = {
    case Calculate =>
      
  }

  private def nextComplex = {
    def between(from: Double, to: Double) = from + (rnd.nextDouble * (to - from))
    val re = between(conf.reFrom, conf.reTo)
    val im = between(conf.imFrom, conf.imTo)
    Complex(re, im)
  }


}
