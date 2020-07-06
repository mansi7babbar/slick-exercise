package carbyne.slick

import akka.actor.{ActorRef, ActorSystem, Props}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object DriverApp extends App {
  val m_db: slick.jdbc.MySQLProfile.backend.DatabaseDef = Database.forURL(
    url = "jdbc:mysql://localhost:3306/api_server_db?useSSL=false",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "admin",
    password = "knoldus")
  implicit val system: ActorSystem = ActorSystem()
  val callersToReportsMapTable = new CallersToReportsMapTable(m_db)
  val worker: ActorRef = system.actorOf(Props(new Worker(callersToReportsMapTable)), "worker")
  val manager: ActorRef = system.actorOf(Props(new Manager(callersToReportsMapTable, worker)), "manager")
  manager ! CreateCallerToReportMap
  system.scheduler.schedule(1 minutes, 1 minutes, manager, ExecuteCRUDOps)
}
