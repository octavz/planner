(ns planner.auth.services
  (:use planner.auth.providers.sql)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            ))

(def default-client-id "1")

(defn get-or-create-token [client-id user-id] 
  (let [tk (get-token-by-user user-id) ]
    (if (cltoken/is-valid? tk) 
      tk
      (cltoken/create-token client-id user-id))))

(defn login [email pass] 
  (let [rec (cluser/authenticate-user email pass)
        user-id (:id rec)
        token (get-or-create-token default-client-id user-id)]
    (:token token)))
