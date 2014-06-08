package services

import repos._

  trait UserService {
    val repo: UserRepo

    def createSession(uid: String): RetService[String]
  }
