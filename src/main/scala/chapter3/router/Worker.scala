package chapter3.router

import akka.actor.Actor
import akka.actor.Actor.Receive
import chapter3.router.Worker.Work

/**
  * Created by josgar on 13/11/2016.
  */
class Worker extends Actor {
  override def receive = {
    case msg: Work => println(s"Received a work message and My Actor Ref $self")
  }
}

object Worker {
  case class Work()
}
