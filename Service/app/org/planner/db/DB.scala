package org.planner.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait DB {
  import play.api.db.slick.Config.driver.simple._
  import scala.slick.model.ForeignKeyAction
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = AccessTokens.ddl ++ Actions.ddl ++ AuthCodes.ddl ++ ClientGrantTypes.ddl ++ Clients.ddl ++ EntityTypes.ddl ++ ExtraFields.ddl ++ ExtraFieldsValues.ddl ++ GrantTypes.ddl ++ Groups.ddl ++ GroupsUsers.ddl ++ Labels.ddl ++ PlayEvolutions.ddl ++ Plugins.ddl ++ PluginsProjects.ddl ++ Projects.ddl ++ Resources.ddl ++ Users.ddl ++ UserSessions.ddl ++ UserStatuses.ddl ++ Verbs.ddl
  
  /** Table description of table access_tokens. Objects of this class serve as prototypes for rows in queries. */
  class AccessTokens(tag: Tag) extends Table[AccessToken](tag, "access_tokens") {
    def * = (accessToken, refreshToken, userId, scope, expiresIn, createdAt, clientId) <> (AccessToken.tupled, AccessToken.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (accessToken.?, refreshToken, userId.?, scope, expiresIn.?, createdAt.?, clientId.?).shaped.<>({r=>import r._; _1.map(_=> AccessToken.tupled((_1.get, _2, _3.get, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column access_token PrimaryKey */
    val accessToken: Column[String] = column[String]("access_token", O.PrimaryKey)
    /** Database column refresh_token  */
    val refreshToken: Column[Option[String]] = column[Option[String]]("refresh_token")
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column scope  */
    val scope: Column[Option[String]] = column[Option[String]]("scope")
    /** Database column expires_in  */
    val expiresIn: Column[Int] = column[Int]("expires_in")
    /** Database column created_at  */
    val createdAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column client_id  */
    val clientId: Column[String] = column[String]("client_id")
  }
  /** Collection-like TableQuery object for table AccessTokens */
  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))
  
  /** Table description of table actions. Objects of this class serve as prototypes for rows in queries. */
  class Actions(tag: Tag) extends Table[Action](tag, "actions") {
    def * = (id, userId, groupId, description, url, verb, secured, permOwner, permGroup, permPublic, created, updated) <> (Action.tupled, Action.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, groupId.?, description, url.?, verb.?, secured.?, permOwner.?, permGroup.?, permPublic.?, created, updated).shaped.<>({r=>import r._; _1.map(_=> Action.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column group_id  */
    val groupId: Column[String] = column[String]("group_id")
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
    /** Database column url  */
    val url: Column[String] = column[String]("url")
    /** Database column verb  */
    val verb: Column[Int] = column[Int]("verb")
    /** Database column secured  */
    val secured: Column[Short] = column[Short]("secured")
    /** Database column perm_owner Default(0) */
    val permOwner: Column[Short] = column[Short]("perm_owner", O.Default(0))
    /** Database column perm_group Default(0) */
    val permGroup: Column[Short] = column[Short]("perm_group", O.Default(0))
    /** Database column perm_public Default(2) */
    val permPublic: Column[Short] = column[Short]("perm_public", O.Default(2))
    /** Database column created Default(None) */
    val created: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created", O.Default(None))
    /** Database column updated Default(None) */
    val updated: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated", O.Default(None))
    
    /** Foreign key referencing Groups (database name actions_group_id_fkey) */
    lazy val relgroup = foreignKey("actions_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name actions_user_id_fkey) */
    lazy val reluser = foreignKey("actions_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Verbs (database name actions_verb_fkey) */
    lazy val relverb = foreignKey("actions_verb_fkey", verb, Verbs)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (url,verb) (database name uq_url_verb) */
    val index1 = index("uq_url_verb", (url, verb), unique=true)
  }
  /** Collection-like TableQuery object for table Actions */
  lazy val Actions = new TableQuery(tag => new Actions(tag))
  
  /** Table description of table auth_codes. Objects of this class serve as prototypes for rows in queries. */
  class AuthCodes(tag: Tag) extends Table[AuthCode](tag, "auth_codes") {
    def * = (authorizationCode, userId, redirectUri, createdAt, scope, clientId, expiresIn) <> (AuthCode.tupled, AuthCode.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (authorizationCode.?, userId.?, redirectUri, createdAt.?, scope, clientId.?, expiresIn.?).shaped.<>({r=>import r._; _1.map(_=> AuthCode.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column authorization_code PrimaryKey */
    val authorizationCode: Column[String] = column[String]("authorization_code", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column redirect_uri  */
    val redirectUri: Column[Option[String]] = column[Option[String]]("redirect_uri")
    /** Database column created_at  */
    val createdAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column scope  */
    val scope: Column[Option[String]] = column[Option[String]]("scope")
    /** Database column client_id  */
    val clientId: Column[String] = column[String]("client_id")
    /** Database column expires_in  */
    val expiresIn: Column[Int] = column[Int]("expires_in")
  }
  /** Collection-like TableQuery object for table AuthCodes */
  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))
  
  /** Table description of table client_grant_types. Objects of this class serve as prototypes for rows in queries. */
  class ClientGrantTypes(tag: Tag) extends Table[ClientGrantType](tag, "client_grant_types") {
    def * = (clientId, grantTypeId) <> (ClientGrantType.tupled, ClientGrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (clientId.?, grantTypeId.?).shaped.<>({r=>import r._; _1.map(_=> ClientGrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column client_id  */
    val clientId: Column[String] = column[String]("client_id")
    /** Database column grant_type_id  */
    val grantTypeId: Column[Int] = column[Int]("grant_type_id")
    
    /** Primary key of ClientGrantTypes (database name pk_client_grant_type) */
    val pk = primaryKey("pk_client_grant_type", (clientId, grantTypeId))
  }
  /** Collection-like TableQuery object for table ClientGrantTypes */
  lazy val ClientGrantTypes = new TableQuery(tag => new ClientGrantTypes(tag))
  
  /** Table description of table clients. Objects of this class serve as prototypes for rows in queries. */
  class Clients(tag: Tag) extends Table[Client](tag, "clients") {
    def * = (id, secret, redirectUri, scope) <> (Client.tupled, Client.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, secret, redirectUri, scope).shaped.<>({r=>import r._; _1.map(_=> Client.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column secret  */
    val secret: Column[Option[String]] = column[Option[String]]("secret")
    /** Database column redirect_uri  */
    val redirectUri: Column[Option[String]] = column[Option[String]]("redirect_uri")
    /** Database column scope  */
    val scope: Column[Option[String]] = column[Option[String]]("scope")
  }
  /** Collection-like TableQuery object for table Clients */
  lazy val Clients = new TableQuery(tag => new Clients(tag))
  
  /** Table description of table entity_types. Objects of this class serve as prototypes for rows in queries. */
  class EntityTypes(tag: Tag) extends Table[EntityType](tag, "entity_types") {
    def * = (id, description) <> (EntityType.tupled, EntityType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, description).shaped.<>({r=>import r._; _1.map(_=> EntityType.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
  }
  /** Collection-like TableQuery object for table EntityTypes */
  lazy val EntityTypes = new TableQuery(tag => new EntityTypes(tag))
  
  /** Table description of table extra_fields. Objects of this class serve as prototypes for rows in queries. */
  class ExtraFields(tag: Tag) extends Table[ExtraField](tag, "extra_fields") {
    def * = (id, name, validation, entityTypeId, parentId, pluginId) <> (ExtraField.tupled, ExtraField.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, validation, entityTypeId.?, parentId, pluginId.?).shaped.<>({r=>import r._; _1.map(_=> ExtraField.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column name  */
    val name: Column[String] = column[String]("name")
    /** Database column validation  */
    val validation: Column[Option[String]] = column[Option[String]]("validation")
    /** Database column entity_type_id  */
    val entityTypeId: Column[String] = column[String]("entity_type_id")
    /** Database column parent_id  */
    val parentId: Column[Option[String]] = column[Option[String]]("parent_id")
    /** Database column plugin_id  */
    val pluginId: Column[String] = column[String]("plugin_id")
    
    /** Foreign key referencing EntityTypes (database name extra_fields_entity_type_id_fkey) */
    lazy val relentity_type = foreignKey("extra_fields_entity_type_id_fkey", entityTypeId, EntityTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing ExtraFields (database name extra_fields_parent_id_fkey) */
    lazy val relextra_field = foreignKey("extra_fields_parent_id_fkey", parentId, ExtraFields)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Plugins (database name extra_fields_plugin_id_fkey) */
    lazy val relplugin = foreignKey("extra_fields_plugin_id_fkey", pluginId, Plugins)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ExtraFields */
  lazy val ExtraFields = new TableQuery(tag => new ExtraFields(tag))
  
  /** Table description of table extra_fields_values. Objects of this class serve as prototypes for rows in queries. */
  class ExtraFieldsValues(tag: Tag) extends Table[ExtraFieldsValue](tag, "extra_fields_values") {
    def * = (id, extraFieldId, entityId, value) <> (ExtraFieldsValue.tupled, ExtraFieldsValue.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, extraFieldId.?, entityId.?, value.?).shaped.<>({r=>import r._; _1.map(_=> ExtraFieldsValue.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column extra_field_id  */
    val extraFieldId: Column[String] = column[String]("extra_field_id")
    /** Database column entity_id  */
    val entityId: Column[String] = column[String]("entity_id")
    /** Database column value  */
    val value: Column[String] = column[String]("value")
    
    /** Foreign key referencing ExtraFields (database name extra_fields_values_extra_field_id_fkey) */
    lazy val relextra_field = foreignKey("extra_fields_values_extra_field_id_fkey", extraFieldId, ExtraFields)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name extra_fields_values_entity_id_fkey) */
    lazy val reluser = foreignKey("extra_fields_values_entity_id_fkey", entityId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ExtraFieldsValues */
  lazy val ExtraFieldsValues = new TableQuery(tag => new ExtraFieldsValues(tag))
  
  /** Table description of table grant_types. Objects of this class serve as prototypes for rows in queries. */
  class GrantTypes(tag: Tag) extends Table[GrantType](tag, "grant_types") {
    def * = (id, grantType) <> (GrantType.tupled, GrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, grantType.?).shaped.<>({r=>import r._; _1.map(_=> GrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column grant_type  */
    val grantType: Column[String] = column[String]("grant_type")
  }
  /** Collection-like TableQuery object for table GrantTypes */
  lazy val GrantTypes = new TableQuery(tag => new GrantTypes(tag))
  
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Groups(tag: Tag) extends Table[Group](tag, "groups") {
    def * = (id, projectId, `type`, name, created, updated) <> (Group.tupled, Group.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, projectId.?, `type`.?, name.?, created, updated).shaped.<>({r=>import r._; _1.map(_=> Group.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column project_id  */
    val projectId: Column[String] = column[String]("project_id")
    /** Database column type Default(0)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Column[Short] = column[Short]("type", O.Default(0))
    /** Database column name  */
    val name: Column[String] = column[String]("name")
    /** Database column created Default(None) */
    val created: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created", O.Default(None))
    /** Database column updated Default(None) */
    val updated: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated", O.Default(None))
    
    /** Foreign key referencing Projects (database name groups_project_id_fkey) */
    lazy val relproject = foreignKey("groups_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Groups */
  lazy val Groups = new TableQuery(tag => new Groups(tag))
  
  /** Table description of table groups_users. Objects of this class serve as prototypes for rows in queries. */
  class GroupsUsers(tag: Tag) extends Table[GroupsUser](tag, "groups_users") {
    def * = (groupId, userId) <> (GroupsUser.tupled, GroupsUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (groupId.?, userId.?).shaped.<>({r=>import r._; _1.map(_=> GroupsUser.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column group_id  */
    val groupId: Column[String] = column[String]("group_id")
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    
    /** Primary key of GroupsUsers (database name groups_users_pkey) */
    val pk = primaryKey("groups_users_pkey", (groupId, userId))
    
    /** Foreign key referencing Groups (database name groups_users_group_id_fkey) */
    lazy val relgroup = foreignKey("groups_users_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name groups_users_user_id_fkey) */
    lazy val reluser = foreignKey("groups_users_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table GroupsUsers */
  lazy val GroupsUsers = new TableQuery(tag => new GroupsUsers(tag))
  
  /** Table description of table labels. Objects of this class serve as prototypes for rows in queries. */
  class Labels(tag: Tag) extends Table[Label](tag, "labels") {
    def * = (id, lang, entityId, entityTypeId, label1, label2, label3) <> (Label.tupled, Label.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, lang.?, entityId.?, entityTypeId.?, label1, label2, label3).shaped.<>({r=>import r._; _1.map(_=> Label.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column lang  */
    val lang: Column[String] = column[String]("lang")
    /** Database column entity_id  */
    val entityId: Column[String] = column[String]("entity_id")
    /** Database column entity_type_id  */
    val entityTypeId: Column[String] = column[String]("entity_type_id")
    /** Database column label1  */
    val label1: Column[Option[String]] = column[Option[String]]("label1")
    /** Database column label2  */
    val label2: Column[Option[String]] = column[Option[String]]("label2")
    /** Database column label3  */
    val label3: Column[Option[String]] = column[Option[String]]("label3")
    
    /** Foreign key referencing EntityTypes (database name labels_entity_type_id_fkey) */
    lazy val relentity_type = foreignKey("labels_entity_type_id_fkey", entityTypeId, EntityTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Labels */
  lazy val Labels = new TableQuery(tag => new Labels(tag))
  
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(tag: Tag) extends Table[PlayEvolution](tag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolution.tupled, PlayEvolution.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, hash.?, appliedAt.?, applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolution.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash  */
    val hash: Column[String] = column[String]("hash")
    /** Database column applied_at  */
    val appliedAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script  */
    val applyScript: Column[Option[String]] = column[Option[String]]("apply_script")
    /** Database column revert_script  */
    val revertScript: Column[Option[String]] = column[Option[String]]("revert_script")
    /** Database column state  */
    val state: Column[Option[String]] = column[Option[String]]("state")
    /** Database column last_problem  */
    val lastProblem: Column[Option[String]] = column[Option[String]]("last_problem")
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))
  
  /** Table description of table plugins. Objects of this class serve as prototypes for rows in queries. */
  class Plugins(tag: Tag) extends Table[Plugin](tag, "plugins") {
    def * = (id, userId, groupId, projectId, description, permOwner, permGroup, permPublic) <> (Plugin.tupled, Plugin.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, groupId.?, projectId.?, description, permOwner.?, permGroup.?, permPublic.?).shaped.<>({r=>import r._; _1.map(_=> Plugin.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column group_id  */
    val groupId: Column[String] = column[String]("group_id")
    /** Database column project_id  */
    val projectId: Column[String] = column[String]("project_id")
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
    /** Database column perm_owner Default(0) */
    val permOwner: Column[Short] = column[Short]("perm_owner", O.Default(0))
    /** Database column perm_group Default(0) */
    val permGroup: Column[Short] = column[Short]("perm_group", O.Default(0))
    /** Database column perm_public Default(2) */
    val permPublic: Column[Short] = column[Short]("perm_public", O.Default(2))
    
    /** Foreign key referencing Groups (database name plugins_group_id_fkey) */
    lazy val relgroup = foreignKey("plugins_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projects (database name plugins_project_id_fkey) */
    lazy val relproject = foreignKey("plugins_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name plugins_user_id_fkey) */
    lazy val reluser = foreignKey("plugins_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Plugins */
  lazy val Plugins = new TableQuery(tag => new Plugins(tag))
  
  /** Table description of table plugins_projects. Objects of this class serve as prototypes for rows in queries. */
  class PluginsProjects(tag: Tag) extends Table[PluginsProject](tag, "plugins_projects") {
    def * = (id, pluginId, projectId, enabled) <> (PluginsProject.tupled, PluginsProject.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, pluginId.?, projectId.?, enabled.?).shaped.<>({r=>import r._; _1.map(_=> PluginsProject.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column plugin_id  */
    val pluginId: Column[String] = column[String]("plugin_id")
    /** Database column project_id  */
    val projectId: Column[String] = column[String]("project_id")
    /** Database column enabled Default(1) */
    val enabled: Column[Short] = column[Short]("enabled", O.Default(1))
    
    /** Foreign key referencing Plugins (database name plugins_projects_plugin_id_fkey) */
    lazy val relplugin = foreignKey("plugins_projects_plugin_id_fkey", pluginId, Plugins)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projects (database name plugins_projects_project_id_fkey) */
    lazy val relproject = foreignKey("plugins_projects_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (pluginId,projectId) (database name uq_plugin_project) */
    val index1 = index("uq_plugin_project", (pluginId, projectId), unique=true)
  }
  /** Collection-like TableQuery object for table PluginsProjects */
  lazy val PluginsProjects = new TableQuery(tag => new PluginsProjects(tag))
  
  /** Table description of table projects. Objects of this class serve as prototypes for rows in queries. */
  class Projects(tag: Tag) extends Table[Project](tag, "projects") {
    def * = (id, userId, name, description, parentId, status, permPublic, created, updated) <> (Project.tupled, Project.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, name.?, description, parentId, status.?, permPublic.?, created, updated).shaped.<>({r=>import r._; _1.map(_=> Project.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column name  */
    val name: Column[String] = column[String]("name")
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
    /** Database column parent_id  */
    val parentId: Column[Option[String]] = column[Option[String]]("parent_id")
    /** Database column status Default(0) */
    val status: Column[Short] = column[Short]("status", O.Default(0))
    /** Database column perm_public Default(2) */
    val permPublic: Column[Short] = column[Short]("perm_public", O.Default(2))
    /** Database column created Default(None) */
    val created: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created", O.Default(None))
    /** Database column updated Default(None) */
    val updated: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated", O.Default(None))
    
    /** Foreign key referencing Projects (database name projects_parent_id_fkey) */
    lazy val relproject = foreignKey("projects_parent_id_fkey", parentId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name projects_user_id_fkey) */
    lazy val reluser = foreignKey("projects_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Projects */
  lazy val Projects = new TableQuery(tag => new Projects(tag))
  
  /** Table description of table resources. Objects of this class serve as prototypes for rows in queries. */
  class Resources(tag: Tag) extends Table[Resource](tag, "resources") {
    def * = (id, content, entityTypeId, userId, groupId, permOwner, permGroup, permPublic, created, updated) <> (Resource.tupled, Resource.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, content, entityTypeId.?, userId.?, groupId.?, permOwner.?, permGroup.?, permPublic.?, created, updated).shaped.<>({r=>import r._; _1.map(_=> Resource.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column content  */
    val content: Column[Option[String]] = column[Option[String]]("content")
    /** Database column entity_type_id  */
    val entityTypeId: Column[String] = column[String]("entity_type_id")
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column group_id  */
    val groupId: Column[String] = column[String]("group_id")
    /** Database column perm_owner Default(0) */
    val permOwner: Column[Short] = column[Short]("perm_owner", O.Default(0))
    /** Database column perm_group Default(0) */
    val permGroup: Column[Short] = column[Short]("perm_group", O.Default(0))
    /** Database column perm_public Default(2) */
    val permPublic: Column[Short] = column[Short]("perm_public", O.Default(2))
    /** Database column created Default(None) */
    val created: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created", O.Default(None))
    /** Database column updated Default(None) */
    val updated: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated", O.Default(None))
    
    /** Foreign key referencing EntityTypes (database name resources_entity_type_id_fkey) */
    lazy val relentity_type = foreignKey("resources_entity_type_id_fkey", entityTypeId, EntityTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Groups (database name resources_group_id_fkey) */
    lazy val relgroup = foreignKey("resources_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name resources_user_id_fkey) */
    lazy val reluser = foreignKey("resources_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resources */
  lazy val Resources = new TableQuery(tag => new Resources(tag))
  
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(tag: Tag) extends Table[User](tag, "users") {
    def * = (id, login, openidType, openidToken, created, updated, lastLogin, status, password, nick) <> (User.tupled, User.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, login.?, openidType.?, openidToken, created.?, updated.?, lastLogin, status.?, password.?, nick.?).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column login  */
    val login: Column[String] = column[String]("login")
    /** Database column openid_type Default(0) */
    val openidType: Column[Int] = column[Int]("openid_type", O.Default(0))
    /** Database column openid_token  */
    val openidToken: Column[Option[String]] = column[Option[String]]("openid_token")
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    /** Database column last_login  */
    val lastLogin: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login")
    /** Database column status Default(0) */
    val status: Column[Int] = column[Int]("status", O.Default(0))
    /** Database column password  */
    val password: Column[String] = column[String]("password")
    /** Database column nick  */
    val nick: Column[String] = column[String]("nick")
    
    /** Foreign key referencing UserStatuses (database name users_status_fkey) */
    lazy val reluser_status = foreignKey("users_status_fkey", status, UserStatuses)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (nick) (database name unique_nick) */
    val index1 = index("unique_nick", nick, unique=true)
    /** Uniqueness Index over (login) (database name users_login_key) */
    val index2 = index("users_login_key", login, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
  
  /** Table description of table user_sessions. Objects of this class serve as prototypes for rows in queries. */
  class UserSessions(tag: Tag) extends Table[UserSession](tag, "user_sessions") {
    def * = (id, userId) <> (UserSession.tupled, UserSession.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?).shaped.<>({r=>import r._; _1.map(_=> UserSession.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
  }
  /** Collection-like TableQuery object for table UserSessions */
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))
  
  /** Table description of table user_statuses. Objects of this class serve as prototypes for rows in queries. */
  class UserStatuses(tag: Tag) extends Table[UserStatus](tag, "user_statuses") {
    def * = (id, description) <> (UserStatus.tupled, UserStatus.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, description).shaped.<>({r=>import r._; _1.map(_=> UserStatus.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
  }
  /** Collection-like TableQuery object for table UserStatuses */
  lazy val UserStatuses = new TableQuery(tag => new UserStatuses(tag))
  
  /** Table description of table verbs. Objects of this class serve as prototypes for rows in queries. */
  class Verbs(tag: Tag) extends Table[Verb](tag, "verbs") {
    def * = (id, description) <> (Verb.tupled, Verb.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, description).shaped.<>({r=>import r._; _1.map(_=> Verb.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
  }
  /** Collection-like TableQuery object for table Verbs */
  lazy val Verbs = new TableQuery(tag => new Verbs(tag))
}
case class AccessToken(accessToken: String, refreshToken: Option[String], userId: String, scope: Option[String], expiresIn: Int, createdAt: java.sql.Timestamp, clientId: String)

case class Action(id: String, userId: String, groupId: String, description: Option[String], url: String, verb: Int, secured: Short, permOwner: Short = 0, permGroup: Short = 0, permPublic: Short = 2, created: Option[java.sql.Timestamp] = None, updated: Option[java.sql.Timestamp] = None)

case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String], createdAt: java.sql.Timestamp, scope: Option[String], clientId: String, expiresIn: Int)

case class ClientGrantType(clientId: String, grantTypeId: Int)

case class Client(id: String, secret: Option[String], redirectUri: Option[String], scope: Option[String])

case class EntityType(id: String, description: Option[String])

case class ExtraField(id: String, name: String, validation: Option[String], entityTypeId: String, parentId: Option[String], pluginId: String)

case class ExtraFieldsValue(id: String, extraFieldId: String, entityId: String, value: String)

case class GrantType(id: Int, grantType: String)

case class Group(id: String, projectId: String, `type`: Short = 0, name: String, created: Option[java.sql.Timestamp] = None, updated: Option[java.sql.Timestamp] = None)

case class GroupsUser(groupId: String, userId: String)

case class Label(id: String, lang: String, entityId: String, entityTypeId: String, label1: Option[String], label2: Option[String], label3: Option[String])

case class PlayEvolution(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String], revertScript: Option[String], state: Option[String], lastProblem: Option[String])

case class Plugin(id: String, userId: String, groupId: String, projectId: String, description: Option[String], permOwner: Short = 0, permGroup: Short = 0, permPublic: Short = 2)

case class PluginsProject(id: String, pluginId: String, projectId: String, enabled: Short = 1)

case class Project(id: String, userId: String, name: String, description: Option[String], parentId: Option[String], status: Short = 0, permPublic: Short = 2, created: Option[java.sql.Timestamp] = None, updated: Option[java.sql.Timestamp] = None)

case class Resource(id: String, content: Option[String], entityTypeId: String, userId: String, groupId: String, permOwner: Short = 0, permGroup: Short = 0, permPublic: Short = 2, created: Option[java.sql.Timestamp] = None, updated: Option[java.sql.Timestamp] = None)

case class User(id: String, login: String, openidType: Int = 0, openidToken: Option[String], created: java.sql.Timestamp, updated: java.sql.Timestamp, lastLogin: Option[java.sql.Timestamp], status: Int = 0, password: String, nick: String)

case class UserSession(id: String, userId: String)

case class UserStatus(id: Int, description: Option[String])

case class Verb(id: Int, description: Option[String])