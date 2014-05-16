package hu.nemzsom.buddhabrot

import akka.actor.{Props, ActorSystem}

object Main extends App {

  val config = Config.default

  val system = ActorSystem("MySystem")

  val mandel = system.actorOf(Props[Calculator], "mandel")

  mandel ! "Hello"

}
