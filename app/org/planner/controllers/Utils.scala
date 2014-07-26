package org.planner.controllers

import play.api.Play
import play.api.templates.Html
import scala.io.Source
import play.api.Play.current

object include {

  def apply(path: String): Html = {
    val p =  Play.application.path
    val p1 = Play.resource("/index.html")
//    println(p1)
    val source = Source.fromFile(p, "UTF-8").getLines().mkString("\n")
    Html(source)
  }

}
