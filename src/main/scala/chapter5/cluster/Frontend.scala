package chapter5.cluster

import java.util.Random

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import chapter5.cluster.Backend.{Add, BackendRegistration}
import com.typesafe.config.ConfigFactory

/**
  * Created by josgar on 20/11/2016.
  */
class Frontend extends Actor {

  var backends = IndexedSeq.empty[ActorRef]

  override def receive: Receive = {
    case Add if (backends.isEmpty) => println("Service is not available at the moment")

    case addOp:Add  => backends(new Random().nextInt(backends.size)) forward addOp

    case BackendRegistration =>
      if (!backends.contains(sender())) {
        backends = backends :+ sender()
        context watch(sender())
      }
    case Terminated(a)=>
      backends filterNot(_ == a)
      context unwatch(a)
  }
}

object Frontend {

  private var _frontend: ActorRef = _

  def initiate(): Unit = {

    val config = ConfigFactory.load().getConfig("Frontend")

    val system = ActorSystem("ClusterSystem", config)

    _frontend = system.actorOf(Props[Frontend], "frontend")
  }

  def getFrontEnd: ActorRef = _frontend
}