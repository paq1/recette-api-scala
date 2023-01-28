package models

import org.mongodb.scala.bson.BsonDocument

trait BsonSerializable[T] { self =>
  def fromBsonDocumentToObject(bsonDocument: BsonDocument): T
}
