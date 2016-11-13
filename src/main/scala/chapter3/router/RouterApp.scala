package chapter3.router

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import chapter3.router.Worker.Work

/**
  * Created by josgar on 13/11/2016.
  */
object RouterApp extends App {

  val system = ActorSystem("Routing")

//  val router = system.actorOf(Props[Router], "router")
  val router = system.actorOf(FromConfig.props(Props[Worker]), "random-router-pool")

  router ! Work()

  router ! Work()

  router ! Work()

  router ! Work()

  router ! Work()

  Thread.sleep(1000)

  system.terminate()
}
