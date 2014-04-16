(ns planner.services.impl
  (:use planner.repos.sql
        validateur.validation
        planner.repos.redis
        planner.views.login
        planner.models.schema
        planner.services.validation
        planner.repos.oauth
        planner.util)
  (:require [clj-time.core :as time]
            [ring.util.response :as rutil]
            [hiccup.core :as hiccup]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]))

(def master-client (get-client "1"))

(def web-client-id (:client-id master-client))

(planner.repos.oauth/init-auth)

(defn dev-req [uid groups params]
  "session structure"
  {:request 
   {:current-session 
    {:user 
     {:id uid 
      :groups groups}} 
    :params params }})

(defn clean-response [data]
  {:data (dissoc data :created :updated :user_id 
                 :group_id :perm_public :perm_group 
                 :perm_owner :status)})

(defn handler-login-get
  "show login form"
  [{req :request}]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (hiccup/html viewLogin) })

(defn do-login [{params :params :as req}]
  (if-let [u (cluser/authenticate-user
               (params :username)
               (params :password))]
    (let [groups (clojure.string/join "," (map :id (:groups u)))]
      (if (= 1 (:status u))
        (let [token (:token (cltoken/create-token web-client-id u)) ]
          (save-cache token {:c web-client-id :u (:id u) :g groups})
          {:access-token token})
        nil))))

#_(def dev-token 
    (:access-token 
      (do-login {:params {:username "aaa@aaa.com" :password "123456"}})))

(defn handler-login-post
  "handle login and redirect in case of success"
  [{req :request}]
  (tryc
    (do-login req)))

(defn login-redirect
  "doc-string"
  [ctx]
  (if-let [at (:access-token ctx)]
    (-> (rutil/redirect "/")
        (rutil/set-cookie :access_token (:access-token ctx)))
    (rutil/redirect "/login?retry")))

(defn handler-token
  "oauth2 token handler"
  [{req :request}]
  (tryc
    (ep/token-handler req)))

(defn handler-logout
  "oauth2 logout handler"
  [{req :request}]
  (comment "do logout"))

(defn generic-get 
  [getter 
   {session :current-session 
    {params :params} :request :as ctx} ]
  (let [off (:offset params)
        lim (:limit params)]
    {:data (getter 
            (:id params) 
            (:user  session)
            (if off (to-int off) 0)
            (if lim (to-int lim) 10))}))

(defn handler-projects-get [ctx]
  (tryc (generic-get get-projects ctx)))

(defn handler-project-save 
  [{{params :params} :request
    json :json {user :user} :current-session :as ctx}]
  (tryc
    (let [rec {:name (:name json) 
               :description (:description json) 
               :parent_id (:parent_id json)} ]
      (clean-response 
        (if (:id json) 
          (generic-update projects user (assoc rec :id (:id json))) 
          (generic-insert projects user (assoc rec :id (uuid))))))))

(defn handler-resources-get [ctx]
  (tryc (generic-get get-resources ctx)))

(defn handler-user-get
  [{{session :current-session} :request :as ctx}]
  (tryc
    (if-let [usr (:data ctx)]
      {:data 
       {:email (:login usr)
        :perm (map :id (get-resources nil usr 0 1000)) }})))

(handler-user-get 
  (assoc 
    (dev-req "1" ["1"] {:id "1"}) 
    :data {:id "1" :login "test@test.com" :groups ["1"]} ))

(defn get-by-id [ent id] 
  (if-let [item (generic-get-by-id ent id)]
    {:data item}))

(defn handler-user-save
  [{req :request json :json :as ctx}]
  (tryc
    (let [{e :email p :password} json
          usr (get-user-by-email e)]
      (if usr
        {:er "Email already exists" :ec (:email-exists errors)}
        {:result (create-user
                   {:id (uuid)
                    :login e
                    :password (cluser/bcrypt p)})}))))

(defn check-user-access
  "evaluates access to the route and verb"
  [uid]
  (when-let [actions (get-all-actions)] ))

(defn check-user
  [{req :request}]
  (try
    (when-let [hval (get-in req [:headers "authorization"])]
      (when-let [session (get-session hval)]
        {:current-session 
         {:user 
          {:id (:u session) 
           :client-id (:c session) 
           :groups (clojure.string/split (:g session) #",")}}} ))
    (catch Exception e nil)))

