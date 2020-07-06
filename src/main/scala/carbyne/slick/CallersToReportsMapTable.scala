package carbyne.slick

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future

class CallersToReportsMapTableScheme(tag: Tag)
  extends Table[CallerToReportMap](tag, "callerstoreportsmaptable") {
  def * : ProvenShape[CallerToReportMap] =
    (reportID, umtsNumber, sipNumber.?, answeredBy, timestamp) <> (CallerToReportMap.tupled, CallerToReportMap.unapply)

  def reportID: Rep[Long] = column[Long]("REPORT_ID", O.PrimaryKey)

  def umtsNumber: Rep[String] = column[String]("UMTS_NUMBER")

  def sipNumber: Rep[String] = column[String]("SIP_NUMBER")

  def answeredBy: Rep[String] = column[String]("ANSWERED_BY")

  def timestamp: Rep[DateTime] = column[DateTime]("TIMESTAMP")
}

class CallersToReportsMapTable(m_db: slick.jdbc.MySQLProfile.backend.DatabaseDef)
  extends TableQuery(new CallersToReportsMapTableScheme(_)) {

  def createCallerToReportMap: Future[Unit] = {
    val callersToReportsMapTableScheme = TableQuery[CallersToReportsMapTableScheme]
    m_db.run(callersToReportsMapTableScheme.schema.create)
  }

  def insertCallerToReportMap(callerToReportMap: CallerToReportMap): Future[Int] = {
    val insertCallerToReportMapDBAction = this += callerToReportMap
    m_db.run(insertCallerToReportMapDBAction)
  }

  def findByAnsweredBy(answeredBy: String): Future[Seq[CallerToReportMap]] = {
    val findByAnsweredByDBAction = this.filter(_.answeredBy === answeredBy).result
    m_db.run(findByAnsweredByDBAction)
  }

  def findByCallerID(callerID: String): Future[Seq[CallerToReportMap]] = {
    val findByCallerIDDBAction = this.filter(row => row.umtsNumber === callerID || row.sipNumber === callerID).result
    m_db.run(findByCallerIDDBAction)
  }

  def updateAnswered(reportID: Long, answeredBy: String): Future[Int] = {
    val updateAnsweredDBAction = this.filter(_.reportID === reportID).map(_.answeredBy).update(answeredBy)
    m_db.run(updateAnsweredDBAction)
  }

  def removeCaller(reportID: Long): Future[Int] = {
    val removeCallerDBAction = this.filter(_.reportID === reportID).delete
    m_db.run(removeCallerDBAction)
  }
}
