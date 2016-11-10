import Aphrodite.{RestartException, ResumeException, StopException}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._

/**
  * Created by josgar on 10/11/2016.
  */



class Aphrodite extends Actor {

  override def preStart(): Unit = {
    println("Aphrodite preStart hook....")
  }

  override def postStop(): Unit = {
    println("Aphrodite postStop....")
  }


  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Aphrodite preRestart hook....")
    super.preRestart(reason, message)
  }


  override def postRestart(reason: Throwable): Unit = {
    println("Aphrodite postStart hook....")
    super.postRestart(reason)
  }

  def receive = {
    case "Resume" => throw ResumeException
    case "Stop" => throw StopException
    case "Restart" => throw RestartException
    case _ => throw new Exception
  }

}

object Aphrodite {
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

class Hera extends Actor {

  var childRef: ActorRef = _

  override def supervisorStrategy  = OneForOneStrategy(maxNrOfRetries = 10 , withinTimeRange =  5 seconds){
    case ResumeException => Resume
    case RestartException => Restart
    case StopException => Stop
    case _ => Escalate
  }


  override def preStart(): Unit = {
    println("Hera preStart hook....")
    childRef = context.actorOf(Props[Aphrodite], "aphrodite")
  }

  override def receive = {
    case msg =>
      println(s"Hera received $msg")
      childRef ! msg
      Thread.sleep(100)

  }
}

object Supervision extends App {

  val system = ActorSystem("monitoring")

  val hera = system.actorOf(Props[Hera], "hera")
  hera ! "Stop"

}
