package org.planner

import org.planner
import org.planner.db.User

import scala.concurrent.Future
import scalaoauth2.provider.AuthInfo

package object services {

  type ServiceError = (Int, String)

  type RetService[T] = Future[Either[ServiceError, T]]
  type AuthData = AuthInfo[User]

  def retService[T](v: T) = Future.successful(Right(v))

  def retServiceErr(errCode: Int, errMessage: String, data: String = "") = Future.successful(Left(errCode, errMessage))

  def retServiceEx(ex: Throwable, data: String = "", errCode: Int = 500) = Future.successful(Left(errCode, ex.getMessage))

  implicit class ErrorExtractor[T](val ret: Either[ServiceError, T]) {

    def errCode = ret match {
      case Left(er) => er._1
      case _ => 0
    }

    def errMessage = ret match {
      case Left(er) => er._2
      case _ => ""
    }

    def value = ret match {
      case Left(er) => None
      case Right(v) => Some(v)
    }
  }

}
