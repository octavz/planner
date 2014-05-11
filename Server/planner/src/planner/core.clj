(ns planner.core  
  (:use planner.repos.sql
        planner.repos.store
        planner.repos.oauth)
  (:require
    [ring.middleware.reload :as reload]
    [ring.middleware.params :refer [wrap-params]]
    [org.httpkit.server :as http-kit]
    [environ.core :refer [env]]
    [compojure.handler :refer [site api]]
    [compojure.core :refer [defroutes routes]]
    [compojure.route :as route]
    [taoensso.timbre :as timbre]
    [taoensso.timbre.appenders.rotor :as rotor]
    [clauth
     [client :refer [client-store clients register-client]]
     [token :refer [token-store]]
     [user :refer [user-store]]
     [auth-code :refer [auth-code-store]]]
    [liberator.core :refer [resource defresource]]
    [liberator.dev :refer [wrap-trace]]
    [planner.routes.auth :refer [auth-routes ]])
  (:gen-class))

(defn log-request [handler]
  (if (env :dev)
    (fn [req]
      (timbre/debug req)
      (handler req))
    handler))

(defn init
  "init will be called once when app is deployed as a servlet on
  an app server such as Tomcat put any initialization code here"
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
  #_(if (env :dev) (parser/cache-off!))

  (timbre/info "planner started successfully"))

(defn destroy
  "destroy will be called when your application
  shuts down, put any clean up code here"
  []
  (timbre/info "planner is shutting down..."))

(defroutes default-routes
  (route/files "/" {:root "../../Client/app"})
  (route/not-found "Not Found"))

(def handler 
  (let [all-routes (apply routes [auth-routes default-routes])]
    (-> all-routes (wrap-trace :header))) )

(def handler-prod
  (let [all-routes (apply routes [auth-routes default-routes])]
    all-routes )) 

(defn dev? [args] (some #{"-dev"} args))

(defn port [args]
  (if-let [port (first (remove #{"-dev"} args))]
    (Integer/parseInt port)
    9090))

(defn -main [& args]
  (init-auth)
  (http-kit/run-server
    (if (dev? args) (reload/wrap-reload (site #'handler)) handler)
    {:port (port args)})
  (timbre/info "server started on port 9090"))


