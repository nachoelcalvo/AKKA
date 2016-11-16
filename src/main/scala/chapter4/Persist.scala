package chapter4

import akka.actor.{ActorSystem, Props}
import chapter4.Counter.{Cmd, Increment}


/**
  * Created by josgar on 16/11/2016.
  */
object Persist extends App {

  val system = ActorSystem("persist")

  val actor = system.actorOf(Props[Counter], "counter")

  actor ! Cmd(Increment(5))

  actor ! Cmd(Increment(5))

  actor ! Cmd(Increment(5))

  actor ! "print"

  Thread.sleep(2000)

  system.terminate()

}
