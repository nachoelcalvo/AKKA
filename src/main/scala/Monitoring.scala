import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}


/**
  * Created by josgar on 10/11/2016.
  */

class Ares(athena: ActorRef) extends Actor {


  override def preStart(): Unit = {
    context.watch(athena)
  }

  override def postStop(): Unit = {
    println("Stopping Ares")
  }

  override def receive = {
    case Terminated => println("Received Terminated")
      context.stop(self)
  }
}


class Athena extends Actor {
  override def receive: Receive = {
    case msg =>
      println(msg)
      println(s"Terminating $self")
      context.stop(self)
  }
}

object Monitoring extends App {

  val system = ActorSystem("monitoring")

  val athena = system.actorOf(Props[Athena], "athena")
  val ares = system.actorOf(Props(classOf[Ares], athena), "ares")

  athena ! "HI!!!"

  system.terminate()

}

