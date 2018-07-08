package v1

import play.api.i18n.Messages

/**
  * Package object for advert.  This is a good place to put implicit conversions.
  */
package object advert {

  /**
    * Converts between AdvertRequest and Messages automatically.
    */
  implicit def requestToMessages[A](implicit r: AdvertRequest[A]): Messages = {
    r.messages
  }
}
