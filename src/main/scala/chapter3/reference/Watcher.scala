package chapter3.reference

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}

/**
  * Created by josgar on 13/11/2016.
  */
class Watcher extends Actor {
  var actorRef: ActorRef = _

  val selection = context.actorSelection("/user/counter")

  selection ! Identify(None)

  def receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor alive ref $ref")
    case ActorIdentity(_, None) =>
      println("No actor alive")
  }
}
