import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive

/**
  * Created by josgar on 10/11/2016.
  */
object Monitoring {

  class Ares(athena: ActorRef)extends Actor {


    override def preStart(): Unit = {
      println("preStart Ares")
      context.watch(athena)
    }

    override def postStop(): Unit = {
      println("postStop Ares")
    }

    override def receive: Receive = ???

  }


  class Athena extends Actor {
    override def receive: Receive = ???
  }
}
