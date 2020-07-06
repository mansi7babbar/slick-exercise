package carbyne.slick

import org.joda.time.DateTime

case class CallerToReportMap(reportID: Long, umtsNumber: String, sipNumber: Option[String], answeredBy: String,
                             timestamp: DateTime = DateTime.now)
