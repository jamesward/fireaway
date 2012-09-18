package controllers

import play.api.mvc.Action
import play.api.mvc.AnyContent

object WebJarAssets {
  
  def at(path: String, file: String): Action[AnyContent] = {
    Assets.at(path, file)
  }

}
