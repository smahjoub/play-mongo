package repositories

import models.Movie
import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import org.joda.time.DateTime
import reactivemongo.api.commands.WriteResult


@Singleton
class MovieRepository @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi
                               ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("movies"))

  def findAll(limit: Int = 100): Future[Seq[Movie]] = {

    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Movie])
        .cursor[Movie](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Movie]]())
    )
  }

  def findOne(id: BSONObjectID): Future[Option[Movie]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[Movie]).one[Movie])
  }

  def create(movie: Movie): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(movie.copy(_creationDate = Some(new DateTime()), _updateDate = Some(new DateTime()))))
  }

  def update(id: BSONObjectID, movie: Movie):Future[WriteResult] = {

    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("_id" -> id),
        movie.copy(
          _updateDate = Some(new DateTime())))
    )
  }

  def delete(id: BSONObjectID):Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
  }


}