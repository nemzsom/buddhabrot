package hu.nemzsom.buddhabrot

import akka.actor.{ActorLogging, Actor}
import scala.util.Random

case object Calculate

class Calculator extends Actor with BuddhaCalc with ActorLogging {

  override def receive = {
    case Calculate =>
  }

  import Main.config

  override val reFrom: Double = config.reFrom
  override val imFrom: Double = config.reTo
  override val imTo:   Double = config.imFrom
  override val reTo:   Double = config.imTo
}
