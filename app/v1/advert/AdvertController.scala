package v1.advert

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class AdvertFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class AdvertController @Inject()(cc: AdvertControllerComponents)(implicit ec: ExecutionContext)
    extends AdvertBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[AdvertFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(AdvertFormInput.apply)(AdvertFormInput.unapply)
    )
  }

  def getAdverts: Action[AnyContent] = AdvertAction.async { implicit request =>
    logger.trace("index: ")
    advertResourceHandler.find.map { adverts =>
      Ok(Json.toJson(adverts))
    }
  }

  def getAdvert(id: String): Action[AnyContent] = AdvertAction.async { implicit request =>
    logger.trace(s"show: id = $id")
    advertResourceHandler.lookup(id).map { advert =>
      Ok(Json.toJson(advert))
    }
  }

  def create: Action[AnyContent] = AdvertAction.async { implicit request =>
    logger.trace("process: ")
    processJsonAdvert()
  }

  def update: Action[AnyContent] = AdvertAction.async { implicit request =>
    logger.trace("process: ")
    processJsonAdvert()
  }

  def delete: Action[AnyContent] = AdvertAction.async { implicit request =>
    logger.trace("process: ")
    processJsonAdvert()
  }


  private def processJsonAdvert[A]()(implicit request: AdvertRequest[A]): Future[Result] = {
    def failure(badForm: Form[AdvertFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: AdvertFormInput) = {
      advertResourceHandler.create(input).map { advert =>
        Created(Json.toJson(advert)).withHeaders(LOCATION -> advert.link)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
