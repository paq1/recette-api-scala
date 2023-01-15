package repository

import core.RecetteRepository
import models.Recette

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RecetteRepositoryImpl @Inject() (implicit
    executionContext: ExecutionContext
) extends RecetteRepository {

}
