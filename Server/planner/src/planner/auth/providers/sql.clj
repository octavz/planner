(ns planner.auth.providers.sql
  (:use 
    korma.core
    korma.db
    planner.models.schema
    planner.util
    )
  (:require 
    [clauth.store :as cl]
  ))

(defdb db planner.models.schema/db-spec)

(defn create-user [user] (insert users (values user)))

(defn update-user [id email password oid-type oid-token]
  (update users
          (set-fields {:password password
                       :updated (now-ts)
                       :login email
                       :openid_type oid-type
                       :openid_value oid-token
                       })
          (where {:id id})))

(defn get-user [id] (first (select users (where {:id id}) (limit 1))))
(defn get-user-by-email [login] (first (select users (where {:login login}) (limit 1))))
(defn delete-user [login] (update users (set-fields {:status 10}) (where {:login login})))

(defn create-token [rec]
  (transaction
    (delete oauth-tokens (where {:user_id (:subject rec) 
                                 :client_id (:client rec) }) ) 
    (insert oauth-tokens (values rec))))
(defn update-token [id token user-id subject expires scope object]
  (update oauth-tokens
          (set-fields {:id token :client_id user-id
                       :subject subject :expires expires
                       :updated (now-ts)
                       :scope scope :object object})
          (where {:id id})))
(defn get-token [id] (first (select oauth-tokens (where {:id id}) (limit 1))))
(defn delete-token [id] (delete oauth-tokens (where {:id [= id]}) ))
(defn get-token-by-user [user-id] (first (select oauth-tokens (where {:user_id user-id}) (limit 1))))

(defn create-client [rec] (insert oauth-clients (values rec)))
(defn update-client [id secret]
  (update oauth-tokens
          (set-fields {:secret id :updated (now-ts)} )
          (where {:id id})))
(defn get-client [id] (first (select oauth-clients (where {:id id}) (limit 1))))
(defn delete-client [id] 
  (transaction
    (delete oauth-codes (where {:client_id [= id]}))
    (delete oauth-clients (where {:client_id [= id]}))))

(defn create-code [rec] (insert oauth-codes (values rec)))
(defn update-code [id code client subject redirect-uri scope object]
  (update oauth-codes
          (set-fields {:id code :client_id client
                       :subject subject :redirect-uri redirect-uri
                       :updated (now-ts) :scope scope :object object})
          (where {:id id})))
(defn get-code [id] (first (select oauth-codes (where {:id id}) (limit 1))))
(defn delete-code [id] (delete oauth-codes (where {:id [= id]})))

(defrecord UserStore []
  cl/Store
  (fetch [this t] (get-user-by-email t))
  (revoke! [this t] (delete-user t))
  (store! [this key_param user] (create-user user))
  (entries [this] (throw (Exception. "entries not implemented for user")))
  (reset-store! [this] (throw (Exception. "reset not implemented for user"))))

(defrecord ClientStore []
  cl/Store
  (fetch [this t] (get-client t))
  (revoke! [this t] (delete-client t))
  (store! [this key_param client] (create-client client))
  (entries [this] (throw (Exception. "entries not implemented for clients")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defrecord CodeStore []
  cl/Store
  (fetch [this t] (get-code t))
  (revoke! [this t] (delete-code t))
  (store! [this key_param client] (create-code client))
  (entries [this] (("entries not implemented for codes")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defrecord TokenStore []
  cl/Store
  (fetch [this t] (get-token t))
  (revoke! [this t] (delete-token t))
  (store! [this key_param rec] (create-token (update-in rec [:subject] :id )))
  (entries [this] (throw (Exception. "entries not implemented for codes")))
  (reset-store! [this] (throw (Exception. "reset not implemented for codes"))))

(defn create-user-store [] (UserStore.))
(defn create-token-store [] (TokenStore.))
(defn create-code-store [] (CodeStore.))
(defn create-client-store [] (ClientStore.))

