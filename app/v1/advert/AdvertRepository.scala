package v1.advert

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

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


class AdvertExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the AdvertRepository.
  */
trait AdvertRepository {
  def create(data: AdvertData)(implicit mc: MarkerContext): Future[AdvertId]

  def list()(implicit mc: MarkerContext): Future[Iterable[AdvertData]]

  def get(id: AdvertId)(implicit mc: MarkerContext): Future[Option[AdvertData]]
}

/**
  * A trivial implementation for the Advert Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class AdvertRepositoryImpl @Inject()()(implicit ec: AdvertExecutionContext) extends AdvertRepository {

  private val logger = Logger(this.getClass)

  private val advertList = List(
    AdvertData(AdvertId("1"), "title 1", "blog advert 1"),
    AdvertData(AdvertId("2"), "title 2", "blog advert 2"),
    AdvertData(AdvertId("3"), "title 3", "blog advert 3"),
    AdvertData(AdvertId("4"), "title 4", "blog advert 4"),
    AdvertData(AdvertId("5"), "title 5", "blog advert 5")
  )

  override def list()(implicit mc: MarkerContext): Future[Iterable[AdvertData]] = {
    Future {
      logger.trace(s"list: ")
      advertList
    }
  }

  override def get(id: AdvertId)(implicit mc: MarkerContext): Future[Option[AdvertData]] = {
    Future {
      logger.trace(s"get: id = $id")
      advertList.find(advert => advert.id == id)
    }
  }

  def create(data: AdvertData)(implicit mc: MarkerContext): Future[AdvertId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
