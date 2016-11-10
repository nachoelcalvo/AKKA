import Checker.{BlackUser, CheckUser, WhiteUser}
import Recorder.NewUser
import Storage.AddUser
import akka.pattern.ask
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration._


/**
  * Created by josgar on 10/11/2016.
  */


case class User(name: String, email: String)

object Checker {

  sealed trait CheckerMsg
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  case class BlackUser(user: User) extends CheckerResponse
  case class WhiteUser(user: User) extends CheckerResponse

  def props = Props[Checker]
}

class Checker extends Actor {

  var blackListed = List(User("Pedro", "pedro@gmail.com"))
  //test

  override def receive = {
    case CheckUser(user) if blackListed.contains(user) =>
      println(s"$user is black listed")
      sender() ! BlackUser(user)

    case CheckUser(user) if !blackListed.contains(user) =>
      println(s"$user is not black listed")
      sender() ! WhiteUser(user)
  }
}

object Recorder {
  sealed trait RecoderMsg
  case class NewUser(user: User) extends RecoderMsg

  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))

}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5 seconds)

  override def receive = {
    case NewUser(user) => checker ? CheckUser(user) map {
      case WhiteUser(user) =>
        println(s" $user not blacklisted")
        storage ! AddUser(user)
      case BlackUser(user) => println(s" $user won't be added since is blacklisted ")
    }
  }
}

object Storage {
  sealed trait StorageMsg
  case class AddUser(user: User) extends StorageMsg
}

class Storage extends Actor {

  var users = List.empty[User]

  def receive = {
    case AddUser(user) =>
      println(s"Adding $user ")
      users = user :: users
  }
}


object TalkToActor extends App {

  var system = ActorSystem("Storer")

  val checker = system.actorOf(Props[Checker], "checker")
  val storage = system.actorOf(Props[Storage], "storage")
  val recorder = system.actorOf(Recorder.props(checker, storage),"recorder")

  val possibleUser = User("Pedro", "pedro@gmail.com")
  val possibleUser2 = User("Nacho", "nacho@gmail.com")


  recorder ! NewUser(possibleUser)
  recorder ! NewUser(possibleUser2)


  Thread.sleep(100)
  system.terminate()
}
