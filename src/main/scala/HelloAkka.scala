import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive

/**
  * Created by josgar on 09/11/2016.
  */


case class WhoToGreat(who: String)

class Greater extends Actor{
  def receive = {
    case WhoToGreat(who) => println(s"Hey $who !!")
  }
}

object HelloAkka extends App {

  val system = ActorSystem("Akka-system")

  val actor = system.actorOf(Props[Greater], "greater")

  actor !  WhoToGreat("Akka actor")
}
