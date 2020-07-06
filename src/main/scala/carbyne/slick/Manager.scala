package carbyne.slick

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Manager(callersToReportsMapTable: CallersToReportsMapTable, worker: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case CreateCallerToReportMap => callersToReportsMapTable.createCallerToReportMap.onComplete {
      case Success(_) => self ! CreateCallerToReportMapResponse
        self ! ExecuteCRUDOps
      case Failure(exception) => log.info(exception.getMessage)
    }

    case CreateCallerToReportMapResponse => log.info("CallerToReportMap table created!")

    case ExecuteCRUDOps =>
      worker ! InsertCallerToReportMap(CallerToReportMap(1L, "100", Some("073977"), "Raanana", DateTime.now))
      worker ! InsertCallerToReportMap(CallerToReportMap(2L, "100", Some("RS"), "Raanana", DateTime.now))
      worker ! InsertCallerToReportMap(CallerToReportMap(3L, "101", Some("073887"), "Modiin", DateTime.now))
      Thread.sleep(10000)
      worker ! FindByAnsweredBy("Raanana")
      worker ! FindByAnsweredBy("Modiin")
      worker ! FindByAnsweredBy("Tel Aviv")
      worker ! FindByCallerID("073977")
      worker ! FindByCallerID("100")
      worker ! FindByCallerID("073887")
      worker ! FindByCallerID("102")
      Thread.sleep(10000)
      worker ! UpdateAnswered(1L, "Reporty")
      worker ! UpdateAnswered(2L, "Reporty")
      worker ! UpdateAnswered(3L, "Reporty")
      Thread.sleep(10000)
      worker ! RemoveCaller(1L)
      worker ! RemoveCaller(2L)
      worker ! RemoveCaller(3L)
  }
}
