package chapter3.reference

import akka.actor.{ActorSystem, Props}

/**
  * Created by josgar on 13/11/2016.
  */
object Watch extends App {

  val system = ActorSystem("Watcher")

  val counter = system.actorOf(Props[Counter], "counter")

  val watcher = system.actorOf(Props[Watcher], "watcher")

  Thread.sleep(2000)

  system.terminate()
}
