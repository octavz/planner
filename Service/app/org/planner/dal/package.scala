package org.planner

import org.planner.db.DB
import play.api.db.DB
import scaldi.Injector

import scala.concurrent.Future
import scala.concurrent.duration.Duration

package object dal {

  type DAO[T] = Future[Either[String, T]]

  def dao[T](v: T): DAO[T] = Future.successful(Right(v))

  trait Caching {
    def set[A](key: String, value: A, expiration: Int = 0): Future[Boolean]

    def get[A](key: String): Future[Option[A]]

    def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A]): Future[A]
  }

  object CacheKeys {
    def session(id: String): String = s"session:$id"
  }

}

