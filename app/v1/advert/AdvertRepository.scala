package v1.advert

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future


final case class AdvertData(id: AdvertId, title: String, body: String)

class AdvertId private(val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object AdvertId {
  def apply(raw: String): AdvertId = {
    require(raw != null)
    new AdvertId(Integer.parseInt(raw))
  }
}

/**
  * A pure non-blocking interface for the AdvertRepository.
  */
trait AdvertRepository {
  def create(data: AdvertData): Future[AdvertId]

  def list(): Future[Iterable[AdvertData]]

  def get(id: AdvertId): Future[Option[AdvertData]]
}

/**
  * A trivial implementation for the Advert Repository.
  */
@Singleton
class AdvertRepositoryImpl @Inject() extends AdvertRepository {

  private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  private val advertList = List(
    AdvertData(AdvertId("1"), "title 1", "blog advert 1"),
    AdvertData(AdvertId("2"), "title 2", "blog advert 2"),
    AdvertData(AdvertId("3"), "title 3", "blog advert 3"),
    AdvertData(AdvertId("4"), "title 4", "blog advert 4"),
    AdvertData(AdvertId("5"), "title 5", "blog advert 5")
  )

  override def list(): Future[Iterable[AdvertData]] = {
    Future.successful {
      logger.trace(s"list: ")
      advertList
    }
  }

  override def get(id: AdvertId): Future[Option[AdvertData]] = {
    Future.successful {
      logger.trace(s"get: id = $id")
      advertList.find(advert => advert.id == id)
    }
  }

  def create(data: AdvertData): Future[AdvertId] = {
    Future.successful {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
