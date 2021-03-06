package v1.advert

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the AdvertResource controller.
  */
class AdvertRouter @Inject()(controller: AdvertController) extends SimpleRouter {
  val prefix = "/v1/adverts"

  override def routes: Routes = {
    case GET(p"/") =>
      controller.getAdverts

    case POST(p"/") =>
      controller.create

    case PUT(p"/") =>
      controller.update

    case DELETE(p"/") =>
      controller.delete

    case GET(p"/$id") =>
      controller.getAdvert(id)
  }

}
