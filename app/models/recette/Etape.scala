package models.recette

import play.api.libs.json.{JsSuccess, JsValue, Json, Reads, Writes}

case class Etape(
    order: Int,
    description: String
)

object Etape {
  implicit val owrites: Writes[Etape] = (o: Etape) => Json.obj(
    "order" -> o.order,
    "description" -> o.description
  )

  implicit val oread: Reads[Etape] = (js: JsValue) => JsSuccess(
    Etape(
      (js \ "order").as[Int],
      (js \ "description").as[String]
    )
  )
}
