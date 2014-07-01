import org.planner.config._
import play.api.GlobalSettings
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport {
  def applicationModule = new MainConf :: new UserConf :: new ProjectConf
 
}
