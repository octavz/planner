(ns planner.routes.auth
  (:use compojure.core)
  (:require [planner.views.layout :as layout]
            [compojure.route :as route]
            [noir.session :as session]
            [noir.response :as resp]
            [clauth
             [middleware :as mw]
             [endpoints :as ep]
             ]
  ))

(defn handle-login [id pass])


(defroutes auth-routes
  (POST "/login" [id pass] (handle-login id pass))
  (route/files "/client/" {:root "../../Client/app"})
)
