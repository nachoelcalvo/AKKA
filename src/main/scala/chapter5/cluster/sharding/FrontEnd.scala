package chapter5.cluster.sharding

import akka.actor.{Actor, ActorLogging, ActorRef}
import scala.concurrent.duration._
import akka.cluster.sharding.ClusterSharding
import chapter5.cluster.sharding.FrontEnd.{Dec, Get, Inc, Tick}
import scala.util.Random

/**
  * Created by josgar on 30/11/2016.
  */
class FrontEnd extends Actor with ActorLogging {

  import context.dispatcher

  val counterRegion: ActorRef = ClusterSharding(context.system).shardRegion(Counter.shardName)

  context.system.scheduler.schedule(3.seconds, 3.seconds, self, Tick(Inc))

  // Every 12 second will send Decrement operation
  context.system.scheduler.schedule(6.seconds, 6.seconds, self, Tick(Dec))

  // Every 30 second will send get operation
  context.system.scheduler.schedule(10.seconds, 10.seconds, self, Tick(Get))


  override def receive: Receive = {
    case Tick(Inc) =>
      log.info(s"Frontend: Send Increment message to shard region.")
      counterRegion ! Counter.CounterMessage(getId, Counter.Increment)
    case Tick(Dec) =>
      log.info(s"Frontend: Send Decrement message to shard region.")
      counterRegion ! Counter.CounterMessage(getId, Counter.Decrement)
    case Tick(Get) =>
      log.info(s"Frontend: Send Get message to shard region.")
      counterRegion ! Counter.CounterMessage(getId, Counter.Get)
  }

  def getId = Random.nextInt(4)
}

object FrontEnd {

  sealed trait Op
  case object Inc extends Op
  case object Dec extends Op
  case object Get extends Op

  case class Tick(op: Op)
}
