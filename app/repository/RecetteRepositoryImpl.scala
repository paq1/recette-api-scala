package repository

import core.RecetteRepository
import models.recette.{Etape, Recette}
import org.bson.BSONObject
import org.mongodb.scala.bson.{BsonArray, BsonValue}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import play.api.Configuration

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure
import scala.util.control.NonFatal

class RecetteRepositoryImpl @Inject() (implicit
    executionContext: ExecutionContext,
    configuration: Configuration
) extends RecetteRepository {

  val uri: String = configuration.underlying.getString("mongodb.recette.uri")
  val dbName: String =
    configuration.underlying.getString("mongodb.recette.db_name")
  val collectionName: String = configuration.underlying.getString(
    "mongodb.recette.collection.recette.name"
  )
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbName)
  override def findAll(limit: Int): Future[List[Recette]] = db
    .getCollection(collectionName)
    .find()
    .map { element: Document =>
      Recette(
        element
          .get("_id")
          .map((v: BsonValue) => v.asObjectId().getValue.toString),
        element
          .get("nom")
          .map((v: BsonValue) => v.asString().getValue)
          .get,
        element
          .get("etapes")
          .map((v: BsonValue) => fromBsonArrayToListEtape(v.asArray()))
          .getOrElse(List.empty[Etape])
      )
    }
    .toFuture()
    .map(_.toList)

  override def insert(recette: Recette): Future[Unit] = db
    .getCollection(collectionName)
    .insertOne(
      Document(
        "nom" -> recette.nom,
        "etape" -> recette
          .etapes
          .map { etape =>
            Document(
              "order" -> etape.order,
              "description" -> etape.description
            )
          }
      )
    )
    .toFuture()
    .map(_ => ())

  private def fromBsonArrayToListEtape(bsonArray: BsonArray): List[Etape] = {
    bsonArray
      .toArray
      .map[Etape] {
        case r: Document => Etape(
          r
            .get("order")
            .map(v => v.asInt32().getValue)
            .get,
          r
            .get("description")
            .map(v => v.asString().getValue)
            .get
        )
        case _ => Etape(1, "Erreur lol")
      }
      .toList
  }
}
