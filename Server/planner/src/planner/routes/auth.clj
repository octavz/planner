(ns planner.routes.auth
  (:use compojure.core
        planner.actions))

(defroutes auth-routes
  (ANY "/login" [] action-login)
  (ANY "/users" [] action-user)
  (ANY "/user/:id" [] action-user)
  (ANY "/logout" [] action-logout)
  (ANY "/resources" [] action-resources)
  (ANY "/token" [] action-token)
  (ANY "/projects" [] action-projects) 
  (ANY "/project/:id" [id] action-projects) 
  (ANY "/project" [] action-projects) 
  (ANY "/load" [] action-load-test) 
  )

