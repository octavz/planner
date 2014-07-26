package org.planner.controllers

import play.api.Play
import play.api.templates.Html
import scala.io.Source
import play.api.Play.current

object include {

  def apply(absolutePath: String): Html = {
    val p =  Play.application.path
    val source = Source.fromFile(s"$p/$absolutePath", "UTF-8").getLines().mkString("\n")
    Html(source)
  }

}
