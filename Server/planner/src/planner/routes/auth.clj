(ns planner.routes.auth
  (:use compojure.core)
  (:require [planner.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [clauth
             [middleware :as mw]
             [endpoints :as ep]
             [client :refer [client-store clients register-client]]
             [token :refer [token-store]]
             [user :refer [user-store]]
             [auth-code :refer [auth-code-store]]]
  ))

(defn handle-login [id pass])


(defroutes auth-routes
  (POST "/login" [id pass] (handle-login id pass))
)
