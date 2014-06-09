import scala.concurrent.Future

package object repos {

  type RetRepo[T] = Future[Either[String, T]]

}

