package chapter3.become


import java.util.Optional

import akka.actor.{Actor, ActorSystem, Props, Stash}
import chapter3.become.UserStorage.{Connect, Create, Disconnect, Operation}

/**
  * Created by josgar on 13/11/2016.
  */

case class User (username: String, email: String)


object UserStorage {

  trait DBOperation
  case object Create extends DBOperation
  case object Read extends DBOperation
  case object Update extends DBOperation
  case object Delete extends DBOperation

  case object Connect
  case object Disconnect

  case class Operation(operation: DBOperation, user: Option[User])
}

class UserStorage extends Actor with Stash{

  def receive: Receive = disconnected

  def disconnected: Actor.Receive = {
    case Connect => println("Connecting to DB")
      unstashAll()
      context.become(connected)

    case _ => stash()
  }

  def connected: Actor.Receive = {

    case Operation(operation, user) => println(s"Performing operation $operation for user $user")

    case Disconnect => println("Disconnecting from DB")
      context.unbecome()
  }
}

object Become extends App {

  val system = ActorSystem("User-Storage")

  val userStorage = system.actorOf(Props[UserStorage])

  userStorage ! Operation(Create, Some(User("nacho", "nacho@gmail.com")))

  userStorage ! Connect

  userStorage ! Disconnect

  Thread.sleep(1000)

  system.terminate()
}