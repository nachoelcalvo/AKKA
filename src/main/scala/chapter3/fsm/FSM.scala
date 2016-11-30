package chapter3.fsm

import akka.actor.{ActorSystem, Props, Stash,   FSM}
import chapter3.fsm.UserStorage._


case class  User (username: String, email: String)

object UserStorage {

  trait DBOperation
  case object Create extends DBOperation
  case object Read extends DBOperation
  case object Update extends DBOperation
  case object Delete extends DBOperation

  case object Connect
  case object Disconnect

  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  sealed trait Data
  case object EmptyData extends Data

  case class Operation(operation: DBOperation, user: Option[User])
}

class UserStorage extends FSM[State, Data] with Stash {

  import UserStorage._

  startWith(Disconnected, EmptyData)

  when(Disconnected) {
    case Event(Connect, _)  => println("Connecting to DB")
      unstashAll()
      goto(Connected) using(EmptyData)

    case Event(_,_) => stash()
      stay using(EmptyData)
  }

  when(Connected) {
    case Event(Operation(operation, user), _) =>
      println(s"Performing operation $operation for user $user")
      stay using(EmptyData)

    case Event(Disconnect, _) => println("Disconnecting from DB")
      goto(Disconnected) using(EmptyData)
  }

  initialize()
}

object FSM extends App {

  import UserStorage._

  val system = ActorSystem("User-Storage")

  val userStorage = system.actorOf(Props[UserStorage])

  userStorage ! Operation(Create, Some(User("nacho", "nacho@gmail.com")))

  userStorage ! Connect

  userStorage ! Disconnect

  Thread.sleep(1000)

  system.terminate()
}