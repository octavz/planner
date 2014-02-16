(ns planner.routes.auth
  (:use compojure.core
        planner.actions)) 

(defroutes auth-routes
  (ANY "/login" [] action-login)
  (ANY "/user/:id" [id] (action-user id))
  (ANY "/user" [] (action-user nil))
  (ANY "/logout" [] action-logout)
  (ANY "/token" [] action-token))

