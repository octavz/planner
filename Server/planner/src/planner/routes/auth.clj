(ns planner.routes.auth
  (:use compojure.core
        planner.actions)) 

(defroutes auth-routes
  (ANY "/login" [] action-login)
  (ANY "/user" [] action-user)
  (ANY "/logout" [] action-logout)
  (ANY "/register" [] action-register)
  (ANY "/token" [] action-token))

