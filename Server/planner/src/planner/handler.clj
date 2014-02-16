(ns planner.handler
  (:require [compojure.core :refer [defroutes]]
            [planner.middleware :as middleware]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [planner.routes.auth :refer [auth-routes]]
            [planner.models.schema :as schema]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info,
     :enabled? true,
     :async? false,
     :max-message-per-msecs nil,
     :fn rotor/appender-fn})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "planner.log", :max-size (* 512 1024), :backlog 10})
  (if (env :dev) (parser/cache-off!))
  
  (timbre/info "planner started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "planner is shutting down..."))

(def app
 (app-handler
   [auth-routes]
   :middleware
   [middleware/template-error-page middleware/log-request]
   :access-rules
   []
   :formats
   [:json-kw :edn]))

