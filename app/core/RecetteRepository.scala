package core

import models.recette.Recette

import scala.concurrent.Future

trait RecetteRepository {
  def findAll(limit: Int = 100): Future[List[Recette]]

  def insert(recette: Recette): Future[Unit]

  def removeOne(id: String): Future[Unit]
}
