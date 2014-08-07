import org.planner.config._

import play.api._
import play.api.mvc._
import scaldi.play.ScaldiSupport

object Global extends WithFilters(CORSFilter()) with ScaldiSupport {
  def applicationModule = new MainConf :: new UserConf :: new ProjectConf :: new UtilsConf
}
