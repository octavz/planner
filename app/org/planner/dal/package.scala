package org.planner


import play.api.libs.json._

import scala.concurrent.Future

package object dal {

  type DAL[T] = Future[T]

  def dal[T](v: T): DAL[T] = Future.successful(v)

  def dalErr[T](error: String): DAL[T] = Future.failed(new Exception(error))


  trait Caching {
    def set[A](key: String, value: A, expiration: Int = 0)(implicit w: Writes[A]): Future[Boolean]

    def get[A](key: String)(implicit r: Reads[A]): Future[Option[A]]

    def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A])(implicit r: Reads[A], w: Writes[A]): Future[A]

    def getOrElseSync[A](key: String, expiration: Int = 0)(orElse: => A)(implicit r: Reads[A], w: Writes[A]): Future[A]
  }

  object CacheKeys {
    def session(id: String): String = s"session:$id"

    def user(id: String): String = s"user:$id"

    def userGroupsIds(id: String): String = s"userGroupIds:$id"

    def userGroups(id: String): String = s"userGroup:$id"

    def byEmail(email: String): String = s"user:email:$email"

  }

}

