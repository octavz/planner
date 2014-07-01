package org.planner.modules.core

import org.planner.modules.AuthData

trait BaseModule {
  private var _authData: AuthData = null

  def authData = _authData
  def authData_=(value: AuthData): Unit = _authData = value
}
