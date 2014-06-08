(ns planner.models.schema
  (:use
    korma.core
    korma.db
    planner.util)
  (:require [clojure.set :as cset]))

(def db-spec
  {:subprotocol "postgresql"
   :make-pool? true
   :minimum-pool-size 10
   :maximum-pool-size 60
   :subname "//localhost/planner"
   :user "postgres"
   :password "root"})

(declare users oauth-tokens oauth-codes oauth-clients )

(defn swap-keys [h] (into {} (map (fn [[k v]] [v k]) h)))

(def map-tokens {:token :id, :client :client_id, :subject :user_id})
(def map-clients {:client-id :id, :client-secret :secret})
(def map-codes {:code :id :client :client_id :secret :client-secret 
                :subject :user_id})

(defn map-prep [mappings ent]  (cset/rename-keys ent mappings))
(defn map-trans [mappings ent] (cset/rename-keys ent (swap-keys mappings)))

(defentity groups)

(defentity users
  (table :users)
  (has-one oauth-tokens)
  (has-one oauth-codes)
  (many-to-many groups :groups_users 
                {:lpk :id :lfk :user_id :rpk :id :rfk :group_id}))

(defentity groups-users 
  (table :groups_users))

(defentity oauth-tokens
  (prepare (partial map-prep map-tokens))
  (transform (partial map-trans map-tokens))
  (table :oauth_tokens)
  (belongs-to oauth-clients)
  (belongs-to users))

(defentity oauth-clients
  (prepare (partial map-prep map-clients))
  (transform (partial map-trans map-clients))
  (table :oauth_clients)
  (has-one oauth-codes))

(defentity oauth-codes
  (prepare (partial map-prep map-codes))
  (transform (partial map-trans map-codes))
  (table :oauth_codes)
  (belongs-to users)
  (belongs-to oauth-clients))

(defentity actions)
(defentity resources)

(defentity projects
  (has-many groups {:fk :project_id}))

