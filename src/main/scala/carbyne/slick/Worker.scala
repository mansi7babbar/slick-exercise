package carbyne.slick

import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Worker(callersToReportsMapTable: CallersToReportsMapTable) extends Actor with ActorLogging {
  override def receive: Receive = {
    case InsertCallerToReportMap(callerToReportMap) =>
      callersToReportsMapTable.insertCallerToReportMap(callerToReportMap).onComplete {
        case Success(value) =>
          self ! InsertCallerToReportMapResponse(value, callerToReportMap)
        case Failure(exception) => log.info(exception.getMessage)
      }

    case FindByAnsweredBy(answeredBy) =>
      callersToReportsMapTable.findByAnsweredBy(answeredBy).onComplete {
        case Success(value) =>
          self ! FindByAnsweredByResponse(value.size, answeredBy)
        case Failure(exception) => log.info(exception.getMessage)
      }

    case FindByCallerID(callerID) =>
      callersToReportsMapTable.findByCallerID(callerID).onComplete {
        case Success(value) =>
          self ! FindByCallerIDResponse(value.size, callerID)
        case Failure(exception) => log.info(exception.getMessage)
      }

    case UpdateAnswered(reportID, answeredBy) =>
      callersToReportsMapTable.updateAnswered(reportID, answeredBy).onComplete {
        case Success(value) =>
          self ! UpdateAnsweredResponse(value, reportID, answeredBy)
        case Failure(exception) => log.info(exception.getMessage)
      }

    case RemoveCaller(reportID) =>
      callersToReportsMapTable.removeCaller(reportID).onComplete {
        case Success(value) =>
          self ! RemoveCallerResponse(value, reportID)
        case Failure(exception) => log.info(exception.getMessage)
      }

    case InsertCallerToReportMapResponse(rows, callerToReportMap) =>
      log.info(s"Inserted $rows rows with data $callerToReportMap")

    case FindByCallerIDResponse(rows, callerID) =>
      log.info(s"Found $rows rows with caller ID: $callerID")

    case FindByAnsweredByResponse(rows, answeredBy) =>
      log.info(s"Found $rows rows with answered By: $answeredBy")

    case UpdateAnsweredResponse(rows, reportID, answeredBy) =>
      log.info(s"Updated $rows rows with report ID: $reportID by updated answered By: $answeredBy")

    case RemoveCallerResponse(rows, reportID) =>
      log.info(s"Removed $rows rows with report ID: $reportID")
  }
}
