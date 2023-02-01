package api.controllers

import api.services.FileTransfertServiceApache
import models.recette.Recette
import play.api.libs.json.Json
import play.api.mvc._
import repository.RecetteRepositoryImpl

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class RecetteController @Inject() (
    val controllerComponents: ControllerComponents,
    val blagueRepositoryImpl: RecetteRepositoryImpl
)(implicit ec: ExecutionContext)
    extends BaseController {

  val fileTransfertService = new FileTransfertServiceApache

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

  def writeFile(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>

      fileTransfertService.tranfertFile()
        .map { _ =>
          Ok("tranfert reussi")
        }
        .recoverWith {
          case NonFatal(_) => Future.successful(Ok("echec"))
        }

  }
}
