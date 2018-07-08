package v1.advert

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying advert information.
  */
case class AdvertResource(id: String, link: String, title: String, body: String)

object AdvertResource {

  /**
    * Mapping to write a AdvertResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[AdvertResource] {
    def writes(advert: AdvertResource): JsValue = {
      Json.obj(
        "id" -> advert.id,
        "link" -> advert.link,
        "title" -> advert.title,
        "body" -> advert.body
      )
    }
  }
}

/**
  * Controls access to the backend data, returning [[AdvertResource]]
  */
class AdvertResourceHandler @Inject()(
                                       routerProvider: Provider[AdvertRouter],
                                       advertRepository: AdvertRepository)(implicit ec: ExecutionContext) {

  def create(advertInput: AdvertFormInput)(implicit mc: MarkerContext): Future[AdvertResource] = {
    val data = AdvertData(AdvertId("999"), advertInput.title, advertInput.body)
    // We don't actually create the advert, so return what we have
    advertRepository.create(data).map { id =>
      createAdvertResource(data)
    }
  }

  def lookup(id: String)(implicit mc: MarkerContext): Future[Option[AdvertResource]] = {
    val advertFuture = advertRepository.get(AdvertId(id))
    advertFuture.map { maybeAdvertData =>
      maybeAdvertData.map { advertData =>
        createAdvertResource(advertData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[AdvertResource]] = {
    advertRepository.list().map { advertDataList =>
      advertDataList.map(advertData => createAdvertResource(advertData))
    }
  }

  private def createAdvertResource(p: AdvertData): AdvertResource = {
    AdvertResource(p.id.toString, routerProvider.get.link(p.id), p.title, p.body)
  }

}
