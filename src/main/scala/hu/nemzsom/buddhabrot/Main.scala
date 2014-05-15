package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}

object Main extends App {

  val system = ActorSystem("MySystem")

  val mandel = system.actorOf(Props[MandelCalc], "mandel")

  mandel ! "Hello"

}
