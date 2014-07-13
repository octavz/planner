package org.planner.modules.dto

import org.planner.util.{Gen, Time}
import org.planner.db._
import play.api.libs.json._
import org.planner.modules.{ResultError, Result}

case class LoginForm(email: String, password: String)

case class UserDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel = {
    val n = Time.now
    User(id = Gen.guid, login = login, openidToken = Some(login), created = n
      , updated = n, lastLogin = None, password = password, nick = login
    )
  }
}

case class GroupDTO(id: Option[String], name: String, projectId: String) {
  def this(model: Group) = this(Some(model.id), model.name, model.projectId)

  def toModel = {
    val n = Time.now
    Group(id = if (id.isDefined) id.get else Gen.guid, name = name, projectId = projectId, created = Some(n), updated = Some(n))
  }
}

case class ProjectDTO(id: Option[String], name: String, desc: Option[String], parent: Option[String]) {

  def this(model: Project) = this(Some(model.id), model.name, model.description, model.parentId)

  def toModel(userId: String) = {
    val n = Time.now
    Project(id = if (id.isDefined) id.get else Gen.guid, userId = userId, name = name, description = desc,
      parentId = parent, created = Some(n), updated = Some(n))
  }
}

case class ProjectListDTO(items: List[ProjectDTO])


trait JsonFormats extends BaseFormats {

  implicit val userDto = Json.format[UserDTO]
  implicit val projectDto = Json.format[ProjectDTO]
  implicit val projectListDto = Json.format[ProjectListDTO]
  implicit val groupDto = Json.format[GroupDTO]

}
