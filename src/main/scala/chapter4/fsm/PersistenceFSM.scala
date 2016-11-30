package chapter4.fsm

import akka.actor.{ActorSystem, Props}
import chapter4.fsm.Account.{CR, DR, Operation}

/**
  * Created by josgar on 20/11/2016.
  */
object PersistenceFSM extends App {

  val system = ActorSystem("persistence-fsm")

  val account = system.actorOf(Props[Account])

  account ! Operation(DR, 100)

  account ! Operation(CR, 50)

  account ! Operation(CR, 100)

  account ! Operation(DR, 100)

  Thread.sleep(1000)

  system.terminate()
}
