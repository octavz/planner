package org.planner.dal

import play.api.Logger
import scredis.Redis

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by Octav on 7/9/2014.
 */
package object impl {

  class RedisCaching extends Caching {

    lazy val client = Redis()

    import client.ec

    override def set[A](key: String, value: A, expiration: Int = 0): Future[Boolean] = {
      try {
        val f = client.setWithOptions(key, value, if (expiration == 0) None else Some(expiration.seconds))
        f recover { case _ => false}
      } catch {
        case ex: Throwable =>
          Logger.error(ex.getMessage)
          Future.successful(false)
      }
    }

    override def get[A](key: String): Future[Option[A]] = {
      try {
        val f = client.get(key) map {
          case Some(value) =>
            try {
              Some(value.asInstanceOf[A])
            } catch {
              case ex: Exception => None
            }
          case _ => None
        }
        f recover { case _ => None}
      } catch {
        case ex: Throwable =>
          Logger.error(ex.getMessage)
          Future.successful(None)
      }
    }

    override def getOrElseSync[A](key: String, expiration: Int = 0)(orElse: => A): Future[A] = getOrElse[A](key, expiration)(Future.successful(orElse))

    override def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A]): Future[A] =
      try {
        def save(value: A): A = {
          client.setWithOptions(key, value, if (expiration == 0) None else Some(expiration.seconds))
          value
        }
        val f = client.get(key) flatMap {
          case Some(v) => Future.successful(v.asInstanceOf[A])
          case _ =>
            orElse map {
              case None => None.asInstanceOf[A]
              case v if v != null => save(v)
              case v => v
            }
        }
        f recoverWith { case _ => orElse}
      } catch {
        case ex: Throwable =>
          Logger.error(ex.getMessage)
          orElse
      }
  }

  class TestCaching extends Caching {
    override def set[A](key: String, value: A, expiration: Int): Future[Boolean] = Future.successful(false)

    override def get[A](key: String): Future[Option[A]] = Future.successful(None)

    override def getOrElse[A](key: String, expiration: Int)(orElse: => Future[A]): Future[A] = orElse

    override def getOrElseSync[A](key: String, expiration: Int)(orElse: => A): Future[A] = Future.successful(orElse)
  }

}
