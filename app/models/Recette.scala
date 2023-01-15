package models

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._


case class Recette(
    _id: Option[BSONObjectID],
    _creationDate: Option[DateTime],
    title: String,
    description: String
)

object Recette {
  implicit val fmt: Format[Recette] = Json.format[Recette]
  implicit object RecetteBSONReader extends BSONDocumentReader[Recette] {
    override def read(bson: BSONDocument): Recette = Recette(
      _id = bson.getAs[BSONObjectID]("_id"),
      _creationDate = bson.getAs[BSONDateTime]("_creationDate").map(date => new DateTime(date.value)),
      title = bson.getAs[String]("title").get,
      description = bson.getAs[String]("description").get
    )
  }

  implicit object RecetteBSONWriter extends BSONDocumentWriter[Recette] {
    override def write(recette: Recette): BSONDocument = BSONDocument(
      "_id" -> recette._id,
      "_creationDate" -> recette._creationDate.map(date => BSONDateTime(date.getMillis)),
      "title" -> recette.title,
      "description" -> recette.description
    )
  }
}
