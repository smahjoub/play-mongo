package models

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

case class Movie(
                  _id:Option[BSONObjectID],
                  _creationDate: Option[DateTime],
                  _updateDate: Option[DateTime],
                  title:String,
                  description:String
                )
object Movie{
  implicit val fmt : Format[Movie] = Json.format[Movie]
  implicit object MovieBSONReader extends BSONDocumentReader[Movie] {
    def read(doc: BSONDocument): Movie = {
      Movie(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[BSONDateTime]("_creationDate").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDateTime]("_updateDate").map(dt => new DateTime(dt.value)),
        doc.getAs[String]("title").get,
        doc.getAs[String]("description").get)
    }
  }

  implicit object MovieBSONWriter extends BSONDocumentWriter[Movie] {
    def write(movie: Movie): BSONDocument = {
      BSONDocument(
        "_id" -> movie._id,
        "_creationDate" -> movie._creationDate.map(date => BSONDateTime(date.getMillis)),
        "_updateDate" -> movie._updateDate.map(date => BSONDateTime(date.getMillis)),
        "title" -> movie.title,
        "description" -> movie.description

      )
    }
  }
}
