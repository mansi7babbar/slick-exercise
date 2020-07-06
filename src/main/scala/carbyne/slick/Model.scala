package carbyne.slick

case object ExecuteCRUDOps

case object CreateCallerToReportMap

case object CreateCallerToReportMapResponse

case class InsertCallerToReportMap(callerToReportMap: CallerToReportMap)

case class InsertCallerToReportMapResponse(rows: Int, callerToReportMap: CallerToReportMap)

case class FindByAnsweredBy(answeredBy: String)

case class FindByAnsweredByResponse(rows: Int, answeredBy: String)

case class FindByCallerID(callerID: String)

case class FindByCallerIDResponse(rows: Int, callerID: String)

case class UpdateAnswered(reportID: Long, answeredBy: String)

case class UpdateAnsweredResponse(rows: Int, reportID: Long, answeredBy: String)

case class RemoveCaller(reportID: Long)

case class RemoveCallerResponse(rows: Int, reportID: Long)
