package models

import play.api.libs.json._


case class Blague(
    _id: Option[String],
    description: String
) extends Serializable

object Blague {
  implicit val owrites: Writes[Blague] = (o: Blague) => Json.obj(
    "id" -> o._id,
    "description" -> o.description
  )

  implicit val oread: Reads[Blague] = (js: JsValue) => JsSuccess(
    Blague(
      (js \ "_id").asOpt[String],
      (js \ "description").as[String]
    )
  )
}
