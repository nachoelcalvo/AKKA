package chapter3.reference

import akka.actor.Actor
import chapter3.reference.Counter.{DecMsg, IncMsg}

/**
  * Created by josgar on 13/11/2016.
  */
class Counter extends Actor {

  var count = 0

  def receive = {
    case IncMsg(x) => count += x
    case DecMsg(x) => count -= x
  }
}

object Counter {
  sealed trait counterMsg
  case class IncMsg(x: Int) extends counterMsg
  case class DecMsg(x: Int) extends counterMsg
}
