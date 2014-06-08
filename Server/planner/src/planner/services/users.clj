(ns planner.services.users
  (:use
        validateur.validation
        planner.repos.redis
        planner.views.login
        planner.services.validation
        planner.repos.oauth
        planner.util)
  (:require
            [planner.models.schema :as models]
            [planner.repos.sql :as repo]
            [clj-time.core :as time]
            [ring.util.response :as rutil]
            [hiccup.core :as hiccup]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep] ))

(def master-client (repo/get-client "1"))

(def web-client-id (:client-id master-client))

(defn dev-req [uid groups params json]
  "request structure"
  {
   :json json
   :request {
             :current-session 
             {:user {
                     :id uid 
                     :groups groups}} 
             :params params }})

(defn handler-login-get
  "show login form"
  [ctx]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (hiccup/html viewLogin) })

(defn do-login [{params :params}]
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
        (rutil/set-cookie :access_token at))
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

(defn handler-user-get
  [{{session :current-session} :request :as ctx}]
  (prn ctx)
  (tryc
    (if-let [usr (:data ctx)]
      {:data 
       {:email (:login usr)
        :perm (map :id (repo/get-resources nil usr 0 1000)) }})))

#_(get-user-by-email "aaa1@aaa.com")

(defn handler-user-create
  [{req :request json :json}]
  (tryc
    (let [{e :email p :password} json
          usr (repo/get-user-by-email e)]
      (if usr
        {:er "Email already exists" :ec (:email-exists errors)}
        {:data (clean-response (repo/create-user
                                 {:id (uuid)
                                  :login e
                                  :password (cluser/bcrypt p)}))}))))

(defn handler-user-update
  [{req :request json :json}]
  (tryc
    (let [{e :email p :password} json
          usr (repo/get-user-by-email e)]
      (if (\= (:id usr) (-> req :current-sesion :user :id))
        {:er "Email already exists" :ec (:email-exists errors)}
        {:data (repo/update-user (uuid) e (cluser/bcrypt p) (:openid_type usr) e)}))))

(defn check-user-access
  "evaluates access to the route and verb"
  [uid]
  (when-let [actions (repo/get-all-actions)] ))

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

(defn handler-resources-get [ctx]
  (tryc (generic-get repo/get-resources ctx)))
