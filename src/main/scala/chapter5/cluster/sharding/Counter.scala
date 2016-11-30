package chapter5.cluster.sharding

import akka.actor.{ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import akka.persistence.PersistentActor
import chapter4.Counter.{Decrement, Increment}
import chapter5.cluster.sharding.Counter.{CounterChanged, Event, Get, Stop}

/**
  * Created by josgar on 30/11/2016.
  */
class Counter extends PersistentActor with ActorLogging {

  var count = 0

  override def receiveRecover = {
    case event: CounterChanged => updateState(event)
  }

  def updateState(event: CounterChanged) = {
    count += event.delta
  }

  override def receiveCommand = {
    case Increment => persist(CounterChanged(+1)) (updateState)
    case Decrement => persist(CounterChanged(-1)) (updateState)
    case Get => sender ! count
    case Stop => context.stop(self)

  }

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name
}

object Counter {

  trait Command
  case object Increment extends Command
  case object Decrement extends Command
  case object Get extends Command
  case object Stop extends Command

  trait Event
  case class CounterChanged(delta: Int) extends Event

  // outside world if he want to send message to sharding should use this message
  case class CounterMessage(id: Long, cmd: Command)

  val idExtractor: ShardRegion.ExtractEntityId = {
    case CounterMessage(id, msg) => (id.toString, msg)
  }

  // shard resolver
  val shardResolver: ShardRegion.ExtractShardId = {
    case CounterMessage(id, msg) => (id % 12).toString
  }

  def props() = Props[Counter]

  val shardName: String = "Counter"
}
