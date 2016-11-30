package chapter4.fsm

import chapter4.fsm.Account._

import scala.reflect._
import akka.persistence.fsm._
import akka.persistence.fsm.PersistentFSM.FSMState

/**
  * Created by josgar on 20/11/2016.
  */
object Account {

  //Define States
  sealed trait State extends FSMState

  case object Active extends State {
    override def identifier: String = "active"
  }

  case object Inactive extends State {
    override def identifier: String = "inactive"
  }

  //Define data
  sealed trait Data {
    val amount: Float
  }
  case object ZeroBalance extends Data {
    override val amount: Float = 0.0f
  }

  case class Balance(override val amount: Float) extends Data


  //Define Domain events
  sealed trait DomainEvent

  case class AcceptedTransaction(amount: Float, transactionType: TransactionType) extends DomainEvent
  case class RejectedTransaction(amount:Float, transactionType: TransactionType, reason:String) extends DomainEvent

  sealed trait TransactionType
  case object CR extends TransactionType
  case object DR extends TransactionType

  //Commands
  case class Operation(operationType: TransactionType, amount: Float)

}


class Account extends PersistentFSM[Account.State, Account.Data, Account.DomainEvent] {

  override def persistenceId: String = "account-mine"

  override def applyEvent(domainEvent: DomainEvent, currentData: Data): Data = {

    domainEvent match {
      case AcceptedTransaction(amount, CR) =>
        val newBalance = new Balance(currentData.amount + amount)
        println(s"The new balance is $newBalance")
        newBalance

      case AcceptedTransaction(amount, DR) =>
        val newBalance = new Balance(currentData.amount - amount)
        println(s"The new balance is $newBalance")
        newBalance

      case RejectedTransaction(amount, _, reason) => println(s"Transaction rejected with $amount")
        println(s"Transaction rejected: $reason")
        println(s"current balance: $currentData")
        currentData
    }
  }

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]

  startWith(Inactive, ZeroBalance)

  when(Inactive){
    case Event(Operation(CR, amount), _) =>
      println("Hi There!, This is your first transaction with us!")
      goto(Active) applying AcceptedTransaction(amount, CR)
    case Event(Operation(DR, amount), _) =>
      println("Sorry, your balance is zero!!")
      stay applying RejectedTransaction(amount, DR, "Zero Balance")
  }

  when(Active) {
    case Event(Operation(CR, amount), _) =>
      println(s"Credit transaction for $amount, accepted !")
      stay applying AcceptedTransaction(amount, CR)

    case Event(Operation(DR, amount), balance) =>
      val newBalance = balance.amount - amount
      if (newBalance > 0) {
        println(s"Debit transaction for $amount, accepted !")
        stay applying AcceptedTransaction(amount, DR)
      }
      else if (newBalance < 0) {
        println(s"Debit Transaction for $amount rejected ")
        stay applying RejectedTransaction(amount, DR, "Insufficient funds")
      }
      else {
        println(s"Debit Transaction for $amount, accepted !")
        goto(Inactive) applying AcceptedTransaction(amount, DR)
      }
  }

}
