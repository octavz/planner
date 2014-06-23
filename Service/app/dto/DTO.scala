package dto

import db._

case class LoginForm(email: String, password: String)

case class UserDTO(login: String, password: String) {

  def this(model: User) = this(model.login.get, model.password.get)

}


