(ns planner.core  
  (:use planner.auth.providers.sql)
  (:require
    [planner.handler :refer [app]]
    [ring.middleware.reload :as reload]
    [org.httpkit.server :as http-kit]
    [taoensso.timbre :as timbre]
    [clauth
     [client :refer [client-store clients register-client]]
     [token :refer [token-store]]
     [user :refer [user-store]]
     [auth-code :refer [auth-code-store]]]
    )
  (:gen-class))

(defn dev? [args] (some #{"-dev"} args))

(defn port [args]
  (if-let [port (first (remove #{"-dev"} args))]
    (Integer/parseInt port)
    9090))

(defn -main [& args]
  (do
    (reset! token-store (create-token-store))
    (reset! auth-code-store (create-code-store))
    (reset! client-store (create-client-store))
    (reset! user-store (create-user-store))
    (http-kit/run-server
      (if (dev? args) (reload/wrap-reload app) app)
      {:port (port args)})
    )
  (timbre/info "server started on port 9090"))


