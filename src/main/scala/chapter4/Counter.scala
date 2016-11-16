package chapter4

import akka.actor.ActorLogging
import akka.persistence._
import chapter4.Counter._
import org.iq80.leveldb.impl.SnapshotImpl


object Counter {

  sealed trait Operation {
    val count: Int
  }

  case class Increment(override val count :Int) extends Operation
  case class Decrement(override val count: Int) extends Operation

  case class Cmd(operation: Operation)
  case class Event(operation: Operation)

  case class State(count: Int)

}

class Counter extends PersistentActor with ActorLogging {

  println("Starting .............")

  var state: State = State(count = 0)

  def updateState(event: Event): Unit = event match {
    case Event(Increment(count)) =>
      state = State(state.count + count)
      takeSnapshot()

    case Event(Decrement(count)) => state =
      State(state.count - count)
      takeSnapshot()
  }

  override def persistenceId: String = "counter-actor"

  override def receiveRecover: Receive = {
    case event: Event => println(s"Receiving message on recovery mood $event")
      updateState(event)
    case SnapshotOffer(_, snapshot:State) => println("Receiving snapshot")
      state = snapshot
    case RecoveryCompleted => println("Recovery of the messages completed !!!!")
  }

  override def receiveCommand: Receive = {
    case cmd @ Cmd(op) =>
      println(s"Counter receive ${cmd}")
      persist(Event(op)) { evt =>
        updateState(evt)
      }
    case "print" => println(s"Current state of the actor is $state")

    case SaveSnapshotSuccess(metadata)=> println("Save Snapshot success !!!!")

    case SaveSnapshotFailure(metadata, reason)=> println("Ouch, no snapshot created")
  }

  def takeSnapshot(): Unit = {
    if(state.count % 5 == 0)
      saveSnapshot(state)
  }
//  override def recovery: Recovery = Recovery.none
}