package api.services

import org.apache.commons.net.ftp.FTPClient
import play.api.Configuration

import java.io.ByteArrayInputStream
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

case class MonExceptionDeConnectionFTP()
    extends Exception("erreur lors de la connection")
case class MonExceptionDeTransfertDeFichier()
    extends Exception("erreur lors du transfert")

class FileTransfertServiceApache(
    configuration: Configuration
)(implicit ec: ExecutionContext) {
  val serverName: String = configuration.underlying.getString("ftp.host")
  val port: Int = configuration.underlying.getInt("ftp.port")
  val userName: String = configuration.underlying.getString("ftp.user")
  val password: String = configuration.underlying.getString("ftp.password")
  val serverFolderPath: String = configuration.underlying.getString("ftp.serverFolderPath")

  def connect(): Future[FTPClient] = {
    Try({
      val client = new FTPClient()
      client.connect(serverName, port)
      if (client.login(userName, password)) {
        Future.successful(client)
      } else {
        Future.failed(MonExceptionDeConnectionFTP())
      }
    }) match {
      case Success(value: Future[FTPClient]) => value
      case Failure(th) => Future.failed(th)
    }
  }

  def tranfertFile(fileName: String): Future[Unit] = {
    connect()
      .flatMap { client =>
        Try({
          client.storeFile(
            s"$serverFolderPath$fileName",
            new ByteArrayInputStream("fichier_test".getBytes())
          )
        }) match {
          case Success(res) =>
            if (res) {
              Future.successful(())
            } else {
              Future.failed(MonExceptionDeTransfertDeFichier())
            }
          case _ => Future.failed(MonExceptionDeTransfertDeFichier())
        }
      }
  }
}
