package controllers

import models.Blague
import play.api.libs.json.Json
import play.api.mvc._
import repository.BlagueRepositoryImpl

import javax.inject._
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BlagueController @Inject()(
    val controllerComponents: ControllerComponents,
    val blagueRepositoryImpl: BlagueRepositoryImpl
)(implicit ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    blagueRepositoryImpl.findAll().map{
      recette => Ok(Json.toJson(recette))
    }
  }

  def create(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    val b = request.body.asJson.get.as[Blague]

    blagueRepositoryImpl
      .insert(b)
      .map(_ => Ok(Json.toJson(b)))
  }
}
