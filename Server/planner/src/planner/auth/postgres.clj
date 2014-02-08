(ns planner.auth.postgres
  (:require [clauth.store :as cl]
  ))
(use 'planner.models.db)

(defn prepare-client [client] (clojure.set/rename-keys client {:client-id :id, :client-secret :secret})) 
(defn prepare-client-ex [client] (clojure.set/rename-keys client {:id :client-id, :secret :client-secret })) 

(defn prepare-user [user] (clojure.set/rename-keys client {:client-id :id, :client-secret :secret})) 
(defn prepare-user-ex [user] (clojure.set/rename-keys client {:id :client-id, :secret :client-secret })) 

(defrecord UserStore []
  cl/Store
  (fetch [this t] (get-user-by-email t))
  (revoke! [this t] (delete-user t))
  (store! [this key_param user] (create-user user))
  (entries [this] (throw (Exception. "entries not implemented for user")))
  (reset-store! [this] (throw (Exception. "reset not implemented for user"))))

(defrecord ClientStore []
  cl/Store
  (fetch [this t] (prepare-client-ex (get-client t)))
  (revoke! [this t] (delete-client t))
  (store! [this key_param client] (create-client (prepare-client client)))
  (entries [this] (throw (Exception. "entries not implemented for clients")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defrecord CodeStore []
  cl/Store
  (fetch [this t] (get-token t))
  (revoke! [this t] (delete-code t))
  (store! [this key_param client] (create-code client))
  (entries [this] (("entries not implemented for codes")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defrecord TokenStore []
  cl/Store
  (fetch [this t] (get-code t))
  (revoke! [this t] (delete-token t))
  (store! [this key_param client] (create-code client))
  (entries [this] (throw (Exception. "entries not implemented for codes")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defn create-user-store [] (UserStore.))
(defn create-token-store [] (TokenStore.))
(defn create-code-store [] (CodeStore.))
(defn create-client-store [] (ClientStore.))
