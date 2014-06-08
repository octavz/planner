package util

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
