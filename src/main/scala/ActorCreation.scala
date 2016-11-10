import MusicController.{PlayMusic, StopMusic}
import MusicPlayer.{Play, Stop}
import akka.actor.{Actor, ActorSystem, Props}


object MusicPlayer {
  sealed trait PlayerMsg
  case object Play extends PlayerMsg
  case object Stop extends PlayerMsg

}

class MusicPlayer extends Actor {
  def receive = {
    case Play => {
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! PlayMusic
    }
    case Stop => println("Come on !!! I don't wanna stop the music")
    case _ => println("Unknown message ")
  }
}


object MusicController {
  sealed trait ControllerMsg
  case object PlayMusic extends ControllerMsg
  case object StopMusic extends ControllerMsg

  def props = Props[MusicController]
}

class MusicController extends Actor{
  def receive = {
    case PlayMusic => println("Starting the music ...")
    case StopMusic => println("Stopping the music ...")
  }
}

object Creation extends App{

  val system = ActorSystem("Music-System")
  val player = system.actorOf(Props[MusicPlayer], "player")

  player ! Play

  system.terminate()
}
