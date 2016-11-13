package chapter3.reference

import akka.actor.{ActorSystem, PoisonPill, Props}

/**
  * Created by josgar on 10/11/2016.
  */
object ActorPath extends App {

  val system = ActorSystem("Actor-Paths")

  val counter = system.actorOf(Props[Counter], "counter")
  println(s"Actor reference counter $counter")

  val selection = system.actorSelection("counter")
  println(s"Actor selection $selection")

  counter ! PoisonPill

  Thread.sleep(1000)

  val counter2 = system.actorOf(Props[Counter], "counter")
  println(s"Actor reference counter2 $counter2")

  val selection2 = system.actorSelection("counter")
  println(s"Actor selection $selection2")

  system.terminate()

}
