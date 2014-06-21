import scala.concurrent.Future

package object dao {

  type RetRepo[T] = Future[Either[String, T]]

}

