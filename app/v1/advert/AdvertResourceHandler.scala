package v1.advert

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying advert information.
  */
case class AdvertResource(id: String, title: String, fuel: String, price: String, isnew: Boolean, mileage: String, first_registration: String)

object AdvertResource {

  /**
    * Mapping to write a AdvertResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[AdvertResource] {
    def writes(advert: AdvertResource): JsValue = {
      Json.obj(
        "id" -> advert.id,
        "title" -> advert.title,
        "fuel" -> advert.fuel,
        "price" -> advert.price,
        "new" -> advert.isnew,
        "mileage" -> advert.mileage,
        "first_registration" -> advert.first_registration
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
    val data = AdvertData(AdvertId("999"), advertInput.title, advertInput.fuel, advertInput.price, advertInput.isnew, advertInput.mileage, advertInput.first_registration)
    // We don't actually create the advert, so return what we have
    advertRepository.create(data).map { id =>
      createAdvertResource(data)
    }
  }

  def update(advertInput: AdvertFormInput)(implicit mc: MarkerContext): Future[AdvertResource] = {
    val data = AdvertData(AdvertId("999"), advertInput.title, advertInput.fuel, advertInput.price, advertInput.isnew, advertInput.mileage, advertInput.first_registration)
    // We don't actually create the advert, so return what we have
    advertRepository.create(data).map { id =>
      createAdvertResource(data)
    }
  }

  def delete(advertInput: AdvertFormInput)(implicit mc: MarkerContext): Future[AdvertResource] = {
    val data = AdvertData(AdvertId("999"), advertInput.title, advertInput.fuel, advertInput.price, advertInput.isnew, advertInput.mileage, advertInput.first_registration)
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
    AdvertResource(p.id.toString, p.title, p.fuel, p.price, p.isnew, p.mileage, p.first_registration)
  }

}
