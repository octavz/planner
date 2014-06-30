import org.planner.modules._
import play.api.GlobalSettings
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport {
  def applicationModule = new MainModule :: new UserModule :: new ProjectModule
}
