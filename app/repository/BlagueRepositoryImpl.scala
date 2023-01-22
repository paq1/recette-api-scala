package repository

import core.BlagueRepository
import models.Blague
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue
import play.api.Configuration

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class BlagueRepositoryImpl @Inject() (implicit
    executionContext: ExecutionContext,
    configuration: Configuration
) extends BlagueRepository {

  val uri: String = configuration.underlying.getString("mongodb.blague.uri")
  val dbName: String =
    configuration.underlying.getString("mongodb.blague.db_name")
  val collectionName: String = configuration.underlying.getString(
    "mongodb.blague.collection.blague.name"
  )
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbName)

  override def findAll(limit: Int): Future[List[Blague]] = db
    .getCollection(collectionName)
    .find()
    .map { element: Document =>
      Blague(
        element
          .get("_id")
          .map((v: BsonValue) => v.asObjectId().getValue.toString),
        element
          .get("description")
          .map((v: BsonValue) => v.asString().getValue)
          .get
      )
    }
    .toFuture()
    .map(_.toList)

  override def insert(blague: Blague): Future[Unit] = db
    .getCollection(collectionName)
    .insertOne(
      Document(
        "description" -> blague.description
      )
    )
    .toFuture()
    .map(_ => ())
}
