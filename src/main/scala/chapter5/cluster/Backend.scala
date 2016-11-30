package chapter5.cluster

import akka.actor.{Actor, ActorSystem, Props, RootActorPath}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import chapter5.cluster.Backend.{Add, BackendRegistration}
import com.typesafe.config.ConfigFactory


/**
  * Created by josgar on 20/11/2016.
  */


class Backend extends Actor {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberUp])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {

    case Add(num1, num2) => println(s"I'm a back end $self and I receive add operations with $num1 and $num2")

    case MemberUp(member) =>
      if(member.hasRole("frontend")) {
        context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! BackendRegistration
    }
  }
}

object Backend {

  case object BackendRegistration;
  case class Add(num1: Any, num2: Any)

  def initiate(port: Int): Unit = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.load().getConfig("Backend"))

    val system = ActorSystem("ClusterSystem", config)

    val backend = system.actorOf(Props[Backend], "backend")

  }
}


