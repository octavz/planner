package org.planner.services.dto

import org.planner.util.{Gen, Time}
import org.planner.db._
import play.api.libs.json._
import org.planner.services.{ServiceError, RetService}

case class LoginForm(email: String, password: String)

case class UserDTO(login: String, password: String) {

  def this(model: User) = this(model.login.get, model.password.get)

  def toModel = {
    val n = Time.now
    User(id = Gen.guid
      , login = Some(login)
      , openidToken = Some(login)
      , created = Some(n)
      , updated = Some(n)
      , lastLogin = None
      , password = Some(password)
    )
  }
}

case class ProjectDTO(name: String, desc: Option[String], parent: Option[String])

trait BaseFormats {
  implicit def genericReqWrite[T](implicit fmt: Writes[T]): Writes[Either[ServiceError, T]] =
    new Writes[Either[ServiceError, T]] {
      def writes(d: Either[ServiceError, T]): JsValue = d match {
        case Right(t) => Json.toJson(t.asInstanceOf[T])
        case Left(err) => Json.obj(
          "errCode" -> err._1,
          "errMessage" -> err._2
        )
      }
    }
}

trait JsonFormats extends BaseFormats {

  implicit val userDto = Json.format[UserDTO]
  implicit val projectDto = Json.format[ProjectDTO]

}