package org.planner.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait DB {
  import play.api.db.slick.Config.driver.simple._
  import scala.slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import scala.slick.jdbc.{GetResult => GR}
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = AccessTokens.ddl ++ Actions.ddl ++ AuthCodes.ddl ++ ClientGrantTypes.ddl ++ Clients.ddl ++ EntityTypes.ddl ++ GrantTypes.ddl ++ Groups.ddl ++ GroupsUsers.ddl ++ Labels.ddl ++ PlayEvolutions.ddl ++ Projects.ddl ++ Resources.ddl ++ Users.ddl ++ UserSessions.ddl ++ UserStatuses.ddl ++ Verbs.ddl
  
  /** GetResult implicit for fetching AccessToken objects using plain SQL queries */
  implicit def GetResultAccessToken(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[AccessToken] = GR{
    prs => import prs._
    AccessToken.tupled((<<[String], <<?[String], <<[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table access_tokens. Objects of this class serve as prototypes for rows in queries. */
  class AccessTokens(tag: Tag) extends Table[AccessToken](tag, "access_tokens") {
    def * = (accessToken, refreshToken, userId, scope, expiresIn, created, clientId) <> (AccessToken.tupled, AccessToken.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (accessToken.?, refreshToken, userId.?, scope, expiresIn.?, created.?, clientId.?).shaped.<>({r=>import r._; _1.map(_=> AccessToken.tupled((_1.get, _2, _3.get, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column client_id  */
    val clientId: Column[String] = column[String]("client_id")
    
    /** Foreign key referencing Clients (database name access_tokens_client_id_fkey) */
    lazy val relclient = foreignKey("access_tokens_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name access_tokens_user_id_fkey) */
    lazy val reluser = foreignKey("access_tokens_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AccessTokens */
  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))
  
  /** GetResult implicit for fetching Action objects using plain SQL queries */
  implicit def GetResultAction(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[Short], e4: GR[java.sql.Timestamp]): GR[Action] = GR{
    prs => import prs._
    Action.tupled((<<[String], <<?[String], <<[String], <<[Int], <<[Short], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table actions. Objects of this class serve as prototypes for rows in queries. */
  class Actions(tag: Tag) extends Table[Action](tag, "actions") {
    def * = (id, description, url, verbId, secured, userId, groupId, perm, created, updated) <> (Action.tupled, Action.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, description, url.?, verbId.?, secured.?, userId.?, groupId.?, perm.?, created.?, updated.?).shaped.<>({r=>import r._; _1.map(_=> Action.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column description  */
    val description: Column[Option[String]] = column[Option[String]]("description")
    /** Database column url  */
    val url: Column[String] = column[String]("url")
    /** Database column verb_id  */
    val verbId: Column[Int] = column[Int]("verb_id")
    /** Database column secured  */
    val secured: Column[Short] = column[Short]("secured")
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column group_id  */
    val groupId: Column[String] = column[String]("group_id")
    /** Database column perm Default(448) */
    val perm: Column[Int] = column[Int]("perm", O.Default(448))
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    
    /** Foreign key referencing Groups (database name actions_group_id_fkey) */
    lazy val relgroup = foreignKey("actions_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name actions_user_id_fkey) */
    lazy val reluser = foreignKey("actions_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Verbs (database name actions_verb_id_fkey) */
    lazy val relverb = foreignKey("actions_verb_id_fkey", verbId, Verbs)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (url,verbId) (database name actions_url_verb_id_key) */
    val index1 = index("actions_url_verb_id_key", (url, verbId), unique=true)
  }
  /** Collection-like TableQuery object for table Actions */
  lazy val Actions = new TableQuery(tag => new Actions(tag))
  
  /** GetResult implicit for fetching AuthCode objects using plain SQL queries */
  implicit def GetResultAuthCode(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[java.sql.Timestamp], e3: GR[Int]): GR[AuthCode] = GR{
    prs => import prs._
    AuthCode.tupled((<<[String], <<[String], <<?[String], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Int]))
  }
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
    
    /** Foreign key referencing Clients (database name auth_codes_client_id_fkey) */
    lazy val relclient = foreignKey("auth_codes_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name auth_codes_user_id_fkey) */
    lazy val reluser = foreignKey("auth_codes_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AuthCodes */
  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))
  
  /** GetResult implicit for fetching ClientGrantType objects using plain SQL queries */
  implicit def GetResultClientGrantType(implicit e0: GR[String], e1: GR[Int]): GR[ClientGrantType] = GR{
    prs => import prs._
    ClientGrantType.tupled((<<[String], <<[Int]))
  }
  /** Table description of table client_grant_types. Objects of this class serve as prototypes for rows in queries. */
  class ClientGrantTypes(tag: Tag) extends Table[ClientGrantType](tag, "client_grant_types") {
    def * = (clientId, grantTypeId) <> (ClientGrantType.tupled, ClientGrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (clientId.?, grantTypeId.?).shaped.<>({r=>import r._; _1.map(_=> ClientGrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column client_id  */
    val clientId: Column[String] = column[String]("client_id")
    /** Database column grant_type_id  */
    val grantTypeId: Column[Int] = column[Int]("grant_type_id")
    
    /** Primary key of ClientGrantTypes (database name client_grant_types_pkey) */
    val pk = primaryKey("client_grant_types_pkey", (clientId, grantTypeId))
    
    /** Foreign key referencing Clients (database name client_grant_types_client_id_fkey) */
    lazy val relclient = foreignKey("client_grant_types_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing GrantTypes (database name client_grant_types_grant_type_id_fkey) */
    lazy val relgrant_type = foreignKey("client_grant_types_grant_type_id_fkey", grantTypeId, GrantTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ClientGrantTypes */
  lazy val ClientGrantTypes = new TableQuery(tag => new ClientGrantTypes(tag))
  
  /** GetResult implicit for fetching Client objects using plain SQL queries */
  implicit def GetResultClient(implicit e0: GR[String], e1: GR[Option[String]]): GR[Client] = GR{
    prs => import prs._
    Client.tupled((<<[String], <<?[String], <<?[String], <<?[String]))
  }
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
  
  /** GetResult implicit for fetching EntityType objects using plain SQL queries */
  implicit def GetResultEntityType(implicit e0: GR[String], e1: GR[Option[String]]): GR[EntityType] = GR{
    prs => import prs._
    EntityType.tupled((<<[String], <<?[String]))
  }
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
  
  /** GetResult implicit for fetching GrantType objects using plain SQL queries */
  implicit def GetResultGrantType(implicit e0: GR[Int], e1: GR[String]): GR[GrantType] = GR{
    prs => import prs._
    GrantType.tupled((<<[Int], <<[String]))
  }
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
  
  /** GetResult implicit for fetching Group objects using plain SQL queries */
  implicit def GetResultGroup(implicit e0: GR[String], e1: GR[Short], e2: GR[Option[String]], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[Group] = GR{
    prs => import prs._
    Group.tupled((<<[String], <<[String], <<[Short], <<[String], <<[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Int]))
  }
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Groups(tag: Tag) extends Table[Group](tag, "groups") {
    def * = (id, projectId, `type`, name, userId, groupId, perm, created, updated, permProject) <> (Group.tupled, Group.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, projectId.?, `type`.?, name.?, userId.?, groupId, perm.?, created.?, updated.?, permProject.?).shaped.<>({r=>import r._; _1.map(_=> Group.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column project_id  */
    val projectId: Column[String] = column[String]("project_id")
    /** Database column type Default(0)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Column[Short] = column[Short]("type", O.Default(0))
    /** Database column name  */
    val name: Column[String] = column[String]("name")
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    /** Database column group_id  */
    val groupId: Column[Option[String]] = column[Option[String]]("group_id")
    /** Database column perm Default(448) */
    val perm: Column[Int] = column[Int]("perm", O.Default(448))
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    /** Database column perm_project Default(1) */
    val permProject: Column[Int] = column[Int]("perm_project", O.Default(1))
    
    /** Foreign key referencing Groups (database name groups_group_id_fkey) */
    lazy val relgroup = foreignKey("groups_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projects (database name groups_project_id_fkey) */
    lazy val relproject = foreignKey("groups_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name groups_user_id_fkey) */
    lazy val reluser = foreignKey("groups_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Groups */
  lazy val Groups = new TableQuery(tag => new Groups(tag))
  
  /** GetResult implicit for fetching GroupsUser objects using plain SQL queries */
  implicit def GetResultGroupsUser(implicit e0: GR[String]): GR[GroupsUser] = GR{
    prs => import prs._
    GroupsUser.tupled((<<[String], <<[String]))
  }
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
  
  /** GetResult implicit for fetching Label objects using plain SQL queries */
  implicit def GetResultLabel(implicit e0: GR[String], e1: GR[Option[String]]): GR[Label] = GR{
    prs => import prs._
    Label.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String]))
  }
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
  }
  /** Collection-like TableQuery object for table Labels */
  lazy val Labels = new TableQuery(tag => new Labels(tag))
  
  /** GetResult implicit for fetching PlayEvolution objects using plain SQL queries */
  implicit def GetResultPlayEvolution(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolution] = GR{
    prs => import prs._
    PlayEvolution.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
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
  
  /** GetResult implicit for fetching Project objects using plain SQL queries */
  implicit def GetResultProject(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Short], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[Project] = GR{
    prs => import prs._
    Project.tupled((<<[String], <<[String], <<[String], <<?[String], <<?[String], <<[Short], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table projects. Objects of this class serve as prototypes for rows in queries. */
  class Projects(tag: Tag) extends Table[Project](tag, "projects") {
    def * = (id, userId, name, description, parentId, status, perm, created, updated) <> (Project.tupled, Project.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, name.?, description, parentId, status.?, perm.?, created.?, updated.?).shaped.<>({r=>import r._; _1.map(_=> Project.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    /** Database column perm Default(448) */
    val perm: Column[Int] = column[Int]("perm", O.Default(448))
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    
    /** Uniqueness Index over (userId,name) (database name projects_user_id_name_key) */
    val index1 = index("projects_user_id_name_key", (userId, name), unique=true)
  }
  /** Collection-like TableQuery object for table Projects */
  lazy val Projects = new TableQuery(tag => new Projects(tag))
  
  /** GetResult implicit for fetching Resource objects using plain SQL queries */
  implicit def GetResultResource(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[Resource] = GR{
    prs => import prs._
    Resource.tupled((<<[String], <<?[String], <<[String], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table resources. Objects of this class serve as prototypes for rows in queries. */
  class Resources(tag: Tag) extends Table[Resource](tag, "resources") {
    def * = (id, content, entityTypeId, userId, groupId, perm, created, updated) <> (Resource.tupled, Resource.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, content, entityTypeId.?, userId.?, groupId.?, perm.?, created.?, updated.?).shaped.<>({r=>import r._; _1.map(_=> Resource.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
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
    /** Database column perm Default(448) */
    val perm: Column[Int] = column[Int]("perm", O.Default(448))
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    
    /** Foreign key referencing Groups (database name resources_group_id_fkey) */
    lazy val relgroup = foreignKey("resources_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name resources_user_id_fkey) */
    lazy val reluser = foreignKey("resources_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resources */
  lazy val Resources = new TableQuery(tag => new Resources(tag))
  
  /** GetResult implicit for fetching User objects using plain SQL queries */
  implicit def GetResultUser(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]], e4: GR[java.sql.Timestamp]): GR[User] = GR{
    prs => import prs._
    User.tupled((<<[String], <<[String], <<[String], <<[Int], <<?[String], <<?[java.sql.Timestamp], <<[Int], <<[String], <<?[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(tag: Tag) extends Table[User](tag, "users") {
    def * = (id, login, nick, provider, providerToken, lastLogin, status, password, userId, groupId, perm, created, updated) <> (User.tupled, User.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, login.?, nick.?, provider.?, providerToken, lastLogin, status.?, password.?, userId, groupId, perm.?, created.?, updated.?).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get, _9, _10, _11.get, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column login  */
    val login: Column[String] = column[String]("login")
    /** Database column nick  */
    val nick: Column[String] = column[String]("nick")
    /** Database column provider Default(0) */
    val provider: Column[Int] = column[Int]("provider", O.Default(0))
    /** Database column provider_token  */
    val providerToken: Column[Option[String]] = column[Option[String]]("provider_token")
    /** Database column last_login  */
    val lastLogin: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login")
    /** Database column status Default(0) */
    val status: Column[Int] = column[Int]("status", O.Default(0))
    /** Database column password  */
    val password: Column[String] = column[String]("password")
    /** Database column user_id  */
    val userId: Column[Option[String]] = column[Option[String]]("user_id")
    /** Database column group_id  */
    val groupId: Column[Option[String]] = column[Option[String]]("group_id")
    /** Database column perm Default(448) */
    val perm: Column[Int] = column[Int]("perm", O.Default(448))
    /** Database column created  */
    val created: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated  */
    val updated: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    
    /** Uniqueness Index over (login) (database name users_login_key) */
    val index1 = index("users_login_key", login, unique=true)
    /** Uniqueness Index over (nick) (database name users_nick_key) */
    val index2 = index("users_nick_key", nick, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
  
  /** GetResult implicit for fetching UserSession objects using plain SQL queries */
  implicit def GetResultUserSession(implicit e0: GR[String]): GR[UserSession] = GR{
    prs => import prs._
    UserSession.tupled((<<[String], <<[String]))
  }
  /** Table description of table user_sessions. Objects of this class serve as prototypes for rows in queries. */
  class UserSessions(tag: Tag) extends Table[UserSession](tag, "user_sessions") {
    def * = (id, userId) <> (UserSession.tupled, UserSession.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?).shaped.<>({r=>import r._; _1.map(_=> UserSession.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id PrimaryKey */
    val id: Column[String] = column[String]("id", O.PrimaryKey)
    /** Database column user_id  */
    val userId: Column[String] = column[String]("user_id")
    
    /** Foreign key referencing Users (database name user_sessions_user_id_fkey) */
    lazy val reluser = foreignKey("user_sessions_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserSessions */
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))
  
  /** GetResult implicit for fetching UserStatus objects using plain SQL queries */
  implicit def GetResultUserStatus(implicit e0: GR[Int], e1: GR[Option[String]]): GR[UserStatus] = GR{
    prs => import prs._
    UserStatus.tupled((<<[Int], <<?[String]))
  }
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
  
  /** GetResult implicit for fetching Verb objects using plain SQL queries */
  implicit def GetResultVerb(implicit e0: GR[Int], e1: GR[Option[String]]): GR[Verb] = GR{
    prs => import prs._
    Verb.tupled((<<[Int], <<?[String]))
  }
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
case class AccessToken(accessToken: String, refreshToken: Option[String], userId: String, scope: Option[String], expiresIn: Int, created: java.sql.Timestamp, clientId: String)

case class Action(id: String, description: Option[String], url: String, verbId: Int, secured: Short, userId: String, groupId: String, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String], createdAt: java.sql.Timestamp, scope: Option[String], clientId: String, expiresIn: Int)

case class ClientGrantType(clientId: String, grantTypeId: Int)

case class Client(id: String, secret: Option[String], redirectUri: Option[String], scope: Option[String])

case class EntityType(id: String, description: Option[String])

case class GrantType(id: Int, grantType: String)

case class Group(id: String, projectId: String, `type`: Short = 0, name: String, userId: String, groupId: Option[String], perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp, permProject: Int = 1)

case class GroupsUser(groupId: String, userId: String)

case class Label(id: String, lang: String, entityId: String, entityTypeId: String, label1: Option[String], label2: Option[String], label3: Option[String])

case class PlayEvolution(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String], revertScript: Option[String], state: Option[String], lastProblem: Option[String])

case class Project(id: String, userId: String, name: String, description: Option[String], parentId: Option[String], status: Short = 0, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class Resource(id: String, content: Option[String], entityTypeId: String, userId: String, groupId: String, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class User(id: String, login: String, nick: String, provider: Int = 0, providerToken: Option[String], lastLogin: Option[java.sql.Timestamp], status: Int = 0, password: String, userId: Option[String], groupId: Option[String], perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class UserSession(id: String, userId: String)

case class UserStatus(id: Int, description: Option[String])

case class Verb(id: Int, description: Option[String])