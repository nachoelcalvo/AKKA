package chapter3.router

import java.util.Random

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import chapter3.router.Worker.Work

/**
  * Created by josgar on 13/11/2016.
  */
class Router extends Actor {

  var routees: List[ActorRef] = _

  override def preStart() = {
    routees = List.fill(5)(context.actorOf(Props[Worker]))
  }

  def receive = {
    case msg: Work =>
      println("Message received!!! ")
      routees(util.Random.nextInt(routees.size)) ! msg
  }
}

class RouterGroup(routees: List[String]) extends Actor {

  def receive = {
    case msg: Work =>
      println("RouterGroup: Message received !!! ")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward  msg
  }
}
