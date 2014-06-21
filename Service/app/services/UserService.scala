package services

import dao._

  trait UserService {
    val repo: UserDAO

    def createSession(uid: String): RetService[String]
  }
