package repository

import core.RecetteRepository
import models.recette.{Etape, Recette}
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonValue}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import play.api.Configuration

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

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
    .map { elementDocument: Document =>
      val element = elementDocument.toBsonDocument()
      Recette.fromBsonDocumentToObject(element)
    }
    .toFuture()
    .map(_.toList)

  override def insert(recette: Recette): Future[Unit] = db
    .getCollection(collectionName)
    .insertOne(
      Document(
        "nom" -> recette.nom,
        "etape" -> recette.etapes
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
}
