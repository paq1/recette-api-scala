package models.recette

import models.BsonSerializable
import org.mongodb.scala.bson.BsonDocument
import play.api.libs.json.{JsSuccess, JsValue, Json, Reads, Writes}

case class Recette (_id: Option[String], nom: String, etapes: List[Etape])

object Recette extends BsonSerializable[Recette] {
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

  override def fromBsonDocumentToObject(bsonDocument: BsonDocument): Recette = Recette(
    Option(
      bsonDocument
        .get("_id")
        .asObjectId()
        .getValue
        .toString
    ),
    bsonDocument
      .get("nom")
      .asString()
      .getValue,
    bsonDocument
      .get("etape")
      .asArray()
      .toArray
      .toList
      .map { c =>
        Etape.fromBsonDocumentToObject(c.asInstanceOf[BsonDocument])
      }
  )

}
