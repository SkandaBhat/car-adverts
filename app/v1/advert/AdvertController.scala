package v1.advert

import javax.inject.Inject
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class AdvertFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class AdvertController @Inject()(
    action: AdvertAction,
    handler: AdvertResourceHandler)(implicit ec: ExecutionContext)
    extends Controller {

  private val form: Form[AdvertFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(AdvertFormInput.apply)(AdvertFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = {
    action.async { implicit request =>
      handler.find.map { adverts =>
        Ok(Json.toJson(adverts))
      }
    }
  }

  def create: Action[AnyContent] = {
    action.async { implicit request =>
      processJsonAdvert()
    }
  }

  def show(id: String): Action[AnyContent] = {
    action.async { implicit request =>
      handler.lookup(id).map { advert =>
        Ok(Json.toJson(advert))
      }
    }
  }

  private def processJsonAdvert[A]()(
      implicit request: AdvertRequest[A]): Future[Result] = {
    def failure(badForm: Form[AdvertFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: AdvertFormInput) = {
      handler.create(input).map { advert =>
        Created(Json.toJson(advert)).withHeaders(LOCATION -> advert.link)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
