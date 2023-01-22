package models.recette

import play.api.libs.json.{JsSuccess, JsValue, Json, Reads, Writes}

case class Recette (_id: Option[String], nom: String, etapes: List[Etape])

object Recette {
  implicit val owrites: Writes[Recette] = (o: Recette) => Json.obj(
    "_id" -> o._id,
    "nom" -> o.nom,
    "etapes" -> o.etapes
  )

  implicit val oread: Reads[Recette] = (js: JsValue) => JsSuccess(
    Recette(
      (js \ "_id").asOpt[String],
      (js \ "nom").as[String],
      (js \ "etapes").as[List[Etape]]
    )
  )
}
