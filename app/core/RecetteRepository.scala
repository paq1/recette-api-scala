package core

import models.Recette

import scala.concurrent.Future

trait RecetteRepository {
  def findAll(limit: Int = 100): Future[List[Recette]]
}
