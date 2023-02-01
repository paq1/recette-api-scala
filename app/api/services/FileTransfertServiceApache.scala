package api.services

import org.apache.commons.net.ftp.FTPClient

import java.io.ByteArrayInputStream
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

case class MonExceptionDeConnectionFTP() extends Exception("erreur lors de la connection")
case class MonExceptionDeTransfertDeFichier() extends Exception("erreur lors du transfert")

class FileTransfertServiceApache()(implicit ec: ExecutionContext) {
  val serverName = "192.168.0.17"
  val port = 21
  val userName = "pierre"
  val mdp = "xxx"

  def connect(): Future[FTPClient] = {
    Try({
      val client = new FTPClient()
      client.connect(serverName, port)
      if (client.login(userName, mdp)) {
        Future.successful(client)
      } else {
        Future.failed(MonExceptionDeConnectionFTP())
      }
    }) match {
      case Success(value: Future[FTPClient]) => value
      case Failure(th) => Future.failed(th)
    }
  }

  def tranfertFile(): Future[Unit] = {
    connect()
      .flatMap { client =>

        Try({
          client.storeFile("homes/pierre/fichier_test.txt", new ByteArrayInputStream("fichier_test".getBytes()))

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
