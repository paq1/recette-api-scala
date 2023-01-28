package controllers

import models.recette.Recette
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import repository.RecetteRepositoryImpl

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RecetteController @Inject() (
    val controllerComponents: ControllerComponents,
    val blagueRepositoryImpl: RecetteRepositoryImpl
)(implicit ec: ExecutionContext)
    extends BaseController {

  def index(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      blagueRepositoryImpl.findAll().map { recette =>
        Ok(Json.toJson(recette))
      }
  }

  def create(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val b = request.body.asJson.get.as[Recette]

      blagueRepositoryImpl
        .insert(b)
        .map(_ => Ok(Json.toJson(b)))
  }
}
