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

  def link(id: AdvertId): String = {
    import com.netaporter.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {

    // get all adverts
    case GET(p"/") =>
      controller.index

    // create an advert
    case POST(p"/") =>
      controller.create

    case GET(p"/$id") =>
      controller.show(id)
  }

}
