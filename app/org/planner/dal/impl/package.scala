package org.planner.dal

import play.api.Logger
import play.api.libs.json._
import scredis._
import scredis.serialization._

import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by Octav on 7/9/2014.
 */
package object impl {

  def async[T](result: T): Future[T] = Future.successful(result)

  trait RedisCaching extends Caching {

    class UTF8Reader extends StringReader("UTF-8")

    lazy val client = Redis()

    import client.dispatcher

    override def set[A](key: String, value: A, expiration: Int = 0)(implicit w: Writes[A]): Future[Boolean] = {
      try {
        val f = client.set(key, Json.toJson(value).toString(), if (expiration == 0) None else Some(expiration.seconds))
        f recover { case _ => false}
      } catch {
        case ex: Throwable =>
          Logger.error(ex.getMessage)
          Future.successful(false)
      }
    }

    override def get[A](key: String)(implicit r: Reads[A]): Future[Option[A]] = {
      try {
        val f = client.get(key) map {
          case Some(value) =>
            try {
              Some(Json.fromJson[A](Json.parse(value.toString)).asInstanceOf[A])
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

    override def getOrElseSync[A](key: String, expiration: Int = 0)(orElse: => A)(implicit r: Reads[A], w: Writes[A]): Future[A] = getOrElse[A](key, expiration)(Future.successful(orElse))

    override def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A])(implicit r: Reads[A], w: Writes[A]): Future[A] =
      try {
        def save(value: A): A = {
          client.set(key, Json.toJson(value).toString(), if (expiration == 0) None else Some(expiration.seconds))
          value
        }
        val f = client.get(key) flatMap {
          case Some(v) => Future.successful{
            val js = Json.parse(v)
            Json.fromJson[A](js).get.asInstanceOf[A]
          }
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

  trait TestCaching extends Caching {
    override def set[A](key: String, value: A, expiration: Int)(implicit w: Writes[A]): Future[Boolean] = Future.successful(false)

    override def get[A](key: String)(implicit w: Reads[A]): Future[Option[A]] = Future.successful(None)

    override def getOrElse[A](key: String, expiration: Int)(orElse: => Future[A])(implicit r: Reads[A], w: Writes[A]): Future[A] = orElse

    override def getOrElseSync[A](key: String, expiration: Int)(orElse: => A)(implicit r: Reads[A], w: Writes[A]): Future[A] = Future.successful(orElse)
  }

}
