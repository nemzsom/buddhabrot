package hu.nemzsom.buddhabrot

import akka.actor.{Props, Actor}

class Main(panel: Panel) extends Actor {

  val display = context.actorOf(Props(new Display(panel)), "display")

  context.become(calculation(0))

  def nextGrid(i: Int): Receive = {
    if (i < grids.size) {
      calculation(i)
    }
    else {
      // TODO combine grids and save image
    }
  }

  def calculation(i: Int): Receive = {
    case _ =>
  }

  override def receive = Actor.emptyBehavior
}
