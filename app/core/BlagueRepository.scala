package core

import models.Blague
import scala.concurrent.Future

trait BlagueRepository {
  def findAll(limit: Int = 100): Future[List[Blague]]
  def insert(blague: Blague): Future[Unit]
}
