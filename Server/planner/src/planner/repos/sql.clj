(ns planner.repos.sql
  (:use 
    korma.core
    korma.db
    planner.models.schema
    planner.repos.redis
    planner.util)
  (:require 
    [korma.sql.fns :as kfn]))

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

(defn get-user [id] 
  (first (select users (where {:id id}) (limit 1))))
(defn get-user-by-email [login] 
  (first (select users (with groups) (where {:login login}) (limit 1))))
(defn delete-user [login] 
  (update users (set-fields {:status 10}) (where {:login login})))

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
(defn get-token [id] 
  (first (select oauth-tokens (where {:id id}) (limit 1))))
(defn delete-token [id] (delete oauth-tokens (where {:id [= id]}) ))
(defn get-token-by-user [user-id] 
  (first (select oauth-tokens (where {:user_id user-id}) (limit 1))))

(defn create-client [rec] (insert oauth-clients (values rec)))
(defn update-client [id secret]
  (update oauth-tokens
          (set-fields {:secret id :updated (now-ts)} )
          (where {:id id})))
(defn get-client [id] 
  (first (select oauth-clients (where {:id id}) (limit 1))))
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
(defn get-code [id] 
  (first (select oauth-codes (where {:id id}) (limit 1))))
(defn delete-code [id] 
  (delete oauth-codes (where {:id [= id]})))

(defn get-all-actions []
  (get-or-else ns-action "all" (fn [] (select actions))))

(defn get-all-resources []
  (get-or-else ns-resource "all" (fn [] (select resources))))

(defmacro max-perm [] `[(sqlfn GREATEST :perm_public :perm_group :perm_owner) :perm])
(defmacro where-perm [q u g body] 
  `(where ~q
          (kfn/pred-and 
            ~body
            (kfn/pred-or 
              (kfn/pred-> :perm_public 0) 
              (kfn/pred-and (kfn/pred-= :user_id ~u) (kfn/pred-> :perm_owner 0))
              (kfn/pred-and (kfn/pred-in :group_id ~g) (kfn/pred-> :perm_group 0)) ))))

(defn get-groups [uid]
  (get-or-else-json
    ns-groups 
    uid 
    (fn[] 
      (select 
        groups 
        (fields :id)
        (join groups-users (= :groups_users.group_id :groups.id))
        (where {:groups_users.user_id uid})))))

(defn get-projects [id usr off lim]
  (get-or-else-json 
    ns-projects
    (:id usr)
    (fn[]
    (select 
      projects 
      (fields :id :name :description (max-perm)) 
      (where-perm (:id usr) (:groups usr) (if id {:id id} true))
      (offset off)
      (limit lim)))))

(defn get-resources [id usr off lim]
  (if usr
    (select 
      resources 
      (fields :id :content (max-perm)) 
      (where-perm (:id usr) (map :id (get-groups (:id usr))) (if id {:id id} true))
      (offset off)
      (limit lim) )))

#_(get-projects nil {:id "1" :groups ["1"]} 0 100)

(defn get-actions [id usr off lim]
  (select 
    actions 
    (fields :id :url (max-perm)) 
    (where-perm (:id usr) (:groups usr) (if id {:id id} true))
    (offset off)
    (limit lim)))

(defn generic-get-by-id [ent id]
  (if id 
    (if-let [res (select ent (where {:id id}))]
      (first res))))

(defn generic-insert [ent user rec]
  (insert ent 
          (values 
            (assoc rec 
                   :user_id (:id user) 
                   :group_id (first (:groups user)) ))))

(defn generic-update [ent user rec]
  (update ent 
          (set-fields 
            (assoc rec 
                   :user_id (:id user) 
                   :group_id (first (:groups user)) ))
          (where {:id rec})))

