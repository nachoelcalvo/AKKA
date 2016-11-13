package chapter3.router

import akka.actor.{ActorSystem, Props}
import chapter3.router.Worker.Work


/**
  * Created by josgar on 13/11/2016.
  */
object RouterGroupApp extends App {

  var system = ActorSystem("router-group")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")
  system.actorOf(Props[Worker], "w4")
  system.actorOf(Props[Worker], "w5")

  val routees = List("/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5"
  )

  val routerGroup = system.actorOf(Props(classOf[RouterGroup], routees))

  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(2000)

  system.terminate()
}