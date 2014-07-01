package org.planner

import org.planner.db.User

import scala.concurrent.Future
import scalaoauth2.provider.AuthInfo

package object modules {

  type ResultError = (Int, String)

  type Result[T] = Future[Either[ResultError, T]]
  type AuthData = AuthInfo[User]

  def result[T](v: T) = Future.successful(Right(v))

  def resultError(errCode: Int, errMessage: String, data: String = "") = Future.successful(Left(errCode, errMessage))

  def resultEx(ex: Throwable, data: String = "", errCode: Int = 500) = Future.successful(Left(errCode, ex.getMessage))

  implicit class ErrorExtractor[T](val ret: Either[ResultError, T]) {

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
