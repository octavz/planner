package  org.planner

import scala.concurrent.Future

package object dal {

  type DAO[T] = Future[Either[String, T]]

  def dao[T](v: T): DAO[T] = Future.successful(Right(v))

}

