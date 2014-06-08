import scala.concurrent.Future

package object services {

  type Error = (Int, String)

  type RetService[T] = Future[Either[Error , T]]

  implicit class ErrorExtractor[T](val ret: Either[Error, T]) {

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
