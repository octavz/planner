package org.planner.util

import java.sql.Timestamp

object Constants {
  val EmptyGroupId  = ""
  val DefaultGroupType = 0
}


object Gen {

  /**
   * generates a GUID
   * @return new GUID
   */
  def guid = java.util.UUID.randomUUID().toString

  /**
   * generates an option GUID
   * @return Option GUID
   */
  def guido = Some(guid) 
}

object Time {
  def now = {
    val now = System.currentTimeMillis() / 1000L
    new Timestamp(now * 1000)
  }

  def nowo =  Some(now)

}
