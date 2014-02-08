(ns planner.models.db
  (:use korma.core
        korma.db
        planner.util
        )
  (:require [planner.models.schema :as schema]
            ))

(defdb db schema/db-spec)

(defentity users)

(defentity oauth-tokens (table :oauth_tokens))

(defentity oauth-clients (table :oauth_clients))

(defentity oauth-codes (table :oauth_codes))

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

(defn create-token [rec] (insert oauth-tokens (values rec)))

(defn update-token [id token user-id subject expires scope object]
  (update oauth-tokens
          (set-fields {:id token :client_id user-id
                       :subject subject :expires expires
                       :updated (now-ts)
                       :scope scope :object object})
          (where {:id id})))

(defn get-token [id] (first (select oauth-tokens (where {:id id}) (limit 1))))

(defn delete-token [id] (delete oauth-tokens (where {:id [= id]}) ))

(defn create-client [rec] (insert oauth-clients (values rec)))

(defn update-client [id secret]
  (update oauth-tokens
          (set-fields {:secret id :updated (now-ts)} )
          (where {:id id})))

(defn get-client [id] (first (select oauth-clients (where {:id id}) (limit 1))))

(defn delete-client [id] 
  (transaction
    (delete oauth-codes (where {:client_id [= id]}))
    (delete oauth-clients (where {:client_id [= id]})) 
    ))

(defn create-code [rec] (insert oauth-codes (values rec)))

(defn update-code [id code client subject redirect-uri scope object]
  (update oauth-codes
          (set-fields {:id code :client_id client
                       :subject subject :redirect-uri redirect-uri
                       :updated (now-ts) :scope scope :object object})
          (where {:id id})))

(defn get-code [id] (first (select oauth-codes (where {:id id}) (limit 1))))

(defn delete-code [id] (delete oauth-codes (where {:id [= id]})))
