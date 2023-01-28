package models.recette

import models.BsonSerializable
import org.mongodb.scala.bson.BsonDocument
import play.api.libs.json.{JsSuccess, JsValue, Json, Reads, Writes}

case class Etape(
    order: Int,
    description: String
)

object Etape extends BsonSerializable[Etape] {
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
  override def fromBsonDocumentToObject(bsonDocument: BsonDocument): Etape = Etape(
    bsonDocument
      .get("order")
      .asInt32()
      .getValue,
    bsonDocument
      .get("description")
      .asString()
      .getValue
  )
}
