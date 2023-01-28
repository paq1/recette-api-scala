package models

import play.api.libs.json.{Json, Writes}

case class MongoId (id: String)
object MongoId {
  implicit val owrites: Writes[MongoId] = (o: MongoId) => Json.obj(
    "_id" -> o.id
  )
}
