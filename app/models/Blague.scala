package models

import play.api.libs.json.{Json, Writes}


case class Blague(
    _id: String,
    description: String
) extends Serializable

object Blague {
  implicit val owrites: Writes[Blague] = (o: Blague) => Json.obj(
    "id" -> o._id,
    "description" -> o.description
  )
}
