package controllers

import javax.inject._
import play.api.mvc._
import repositories.MovieRepository
import reactivemongo.bson.BSONObjectID
import play.api.libs.json.{Json, __}
import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future}

import models.Movie
import play.api.libs.json.JsValue

@Singleton
class MovieController @Inject()(
                                 implicit executionContext: ExecutionContext,
                                 val movieRepository: MovieRepository,
                                 val controllerComponents: ControllerComponents)
  extends BaseController {
  def findAll():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    movieRepository.findAll().map {
      movies => Ok(Json.toJson(movies))
    }
  }

  def findOne(id:String):Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => movieRepository.findOne(objectId).map {
        movie => Ok(Json.toJson(movie))
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the movie id"))
    }
  }

  def create():Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[Movie].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      movie =>
        movieRepository.create(movie).map {
          _ => Created(Json.toJson(movie))
        }
    )
  }}

  def update(
              id: String):Action[JsValue]  = Action.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[Movie].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      movie =>{
        val objectIdTryResult = BSONObjectID.parse(id)
        objectIdTryResult match {
          case Success(objectId) => movieRepository.update(objectId, movie).map {
            result => Ok(Json.toJson(result.ok))
          }
          case Failure(_) => Future.successful(BadRequest("Cannot parse the movie id"))
        }
      }
    )
  }}

  def delete(id: String):Action[AnyContent]  = Action.async { implicit request => {
    val objectIdTryResult = BSONObjectID.parse(id)
    objectIdTryResult match {
      case Success(objectId) => movieRepository.delete(objectId).map {
        _ => NoContent
      }
      case Failure(_) => Future.successful(BadRequest("Cannot parse the movie id"))
    }
  }}
}