(ns planner.repos.oauth
  (:use 
    planner.repos.sql
    korma.core
    korma.db
    planner.models.schema
    planner.util)
  (:require 
    [clauth
     [client :refer [client-store clients register-client]]
     [token :refer [token-store]]
     [user :refer [user-store]]
     [auth-code :refer [auth-code-store]]]
    [clauth.store :as cl]))

(defrecord UserStore []
  cl/Store
  (fetch [this t] (get-user-by-email t))
  (revoke! [this t] (delete-user t))
  (store! [this key_param user] (create-user user))
  (entries [this] 
    (throw (Exception. "entries not implemented for user")))
  (reset-store! [this] 
    (throw (Exception. "reset not implemented for user"))))

(defrecord ClientStore []
  cl/Store
  (fetch [this t] (get-client t))
  (revoke! [this t] (delete-client t))
  (store! [this key_param client] (create-client client))
  (entries [this] 
    (throw (Exception. "entries not implemented for clients")))
  (reset-store! [this] 
    (throw (Exception. "reset not implemented for codes"))))

(defrecord CodeStore []
  cl/Store
  (fetch [this t] (get-code t))
  (revoke! [this t] (delete-code t))
  (store! [this key_param client] (create-code client))
  (entries [this] (("entries not implemented for codes")))
  (reset-store! [this] 
    (throw (Exception. "reset not implemented for codes"))))

(defrecord TokenStore []
  cl/Store
  (fetch [this t] (get-token t))
  (revoke! [this t] (delete-token t))
  (store! [this key_param rec] 
    (create-token (update-in rec [:subject] :id )))
  (entries [this] 
    (throw (Exception. "entries not implemented for codes")))
  (reset-store! [this] 
    (throw (Exception. "reset not implemented for codes"))))

(defn create-user-store [] (UserStore.))
(defn create-token-store [] (TokenStore.))
(defn create-code-store [] (CodeStore.))
(defn create-client-store [] (ClientStore.))

(defn init-auth 
  []
  (reset! token-store (create-token-store))
  (reset! auth-code-store (create-code-store))
  (reset! client-store (create-client-store))
  (reset! user-store (create-user-store)))

