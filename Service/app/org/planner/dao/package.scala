package  org.planner

import scala.concurrent.Future

package object dao {

  type RetRepo[T] = Future[Either[String, T]]

  def retRepo[T](v: T): RetRepo[T] = Future.successful(Right(v))

}

