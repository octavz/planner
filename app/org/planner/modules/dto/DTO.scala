package org.planner.modules.dto

import org.planner.util.{Gen, Time}
import org.planner.db._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class LoginForm(email: String, password: String)

case class RegisterDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel = {
    val n = Time.now
    User(id = Gen.guid, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = login, userId = None, groupId = None)
  }
}

case class UserDTO(login: String, password: String, id: String, nick: String) {

  def this(model: User) = this(model.login, model.password, model.id, model.nick)

  def toModel = {
    val n = Time.now
    User(id = id, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = nick, userId = None, groupId = None)
  }

}

case class GroupDTO(id: Option[String], name: String, projectId: String) {
  def this(model: Group) = this(Some(model.id), model.name, model.projectId)

  def toModel(userId: String) = {
    val n = Time.now
    Group(id = id.getOrElse(Gen.guid), name = name, projectId = projectId, userId = userId, groupId = None, created = n, updated = n)
  }
}

case class ProjectDTO(id: Option[String], name: String, desc: Option[String], parent: Option[String], public: Boolean, perm: Option[Int]) {

  def this(model: Project, group: Group) = this(Some(model.id), model.name, model.description, model.parentId, model.perm == 1, perm = Some(group.permProject))

  def toModel(userId: String) = {
    val n = Time.now
    Project(id = id.getOrElse(Gen.guid), userId = userId, name = name, description = desc,
      parentId = parent, created = n, updated = n, perm = if (public) 1 else 0)
  }
}

case class ProjectListDTO(items: List[ProjectDTO])


trait JsonFormats extends BaseFormats with ConstraintReads {

  implicit val userDTO = Json.format[UserDTO]

  implicit val registerDTO = (
    (__ \ 'login).format[String](maxLength[String](200) keepAnd email) ~
      (__ \ 'password).format[String](minLength[String](6) keepAnd maxLength[String](50))
    )(RegisterDTO, unlift(RegisterDTO.unapply))

  implicit val projectDtoRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](5) keepAnd maxLength[String](200)) ~
      (__ \ 'desc).readNullable[String](maxLength[String](1500)) ~
      (__ \ 'parent).readNullable[String](maxLength[String](50)) ~
      (__ \ 'public).read[Boolean] ~
      (__ \ 'perm).readNullable[Int](max(999))
    )(ProjectDTO)

  implicit val projectDtoWrite = Json.writes[ProjectDTO]

  implicit val projectListDto = Json.format[ProjectListDTO]

  implicit val groupDtoRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](5) keepAnd maxLength[String](200)) ~
      (__ \ 'projectId).read[String](maxLength[String](50))
    )(GroupDTO)

  implicit val groupDtoWrite = Json.writes[GroupDTO]

}
