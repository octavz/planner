(ns planner.services.impl
  (:use planner.repos.sql
        validateur.validation
        planner.repos.redis
        planner.util)
  (:require [clj-time.core :as time]
            [ring.util.response :as rutil]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]))

(def master-client (get-client "1"))

(def web-client-id (:client-id master-client))

(defn handler-login-get
  "show login form"
  [{req :request}]
  (use-layout "Login" ((ep/login-handler master-client) req)))

(defn oauth-login
  [req]
  ((ep/login-handler 
     {:client web-client-id 
      :user-authenticator
      (fn [login password]
        #_(prn (cluser/authenticate-user login password))
        (if-let [u (cluser/authenticate-user login password)]
          (if (= 1 (:status u)) u nil)))
      :token-creator 
      (fn [client user]
        (when-let [token (cltoken/create-token client user)]
          (save-session (:token token) {:client client
                                        :uid (:id user)})
          token))
      }) req))

(defn handler-login-post
  "handle login and redirect in case of success"
  [{req :request}]
  (tryc
    (when-let [res (oauth-login req) ]
      (when-let [at (-> res :session :access_token) ]
        {:access-token at}))))

(defn login-redirect 
  "doc-string"
  [ctx]
  (prn ctx)
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

(defn handler-user-save 
  "creates or updates user"
  [{req :request} id]
  {:entry {:id (if (nil? id) "post-id" (str "put-id" id)) :name "post"}})

(defn handler-user-get
  "get user by id"
  [{req :request sess :current-session :as ctx}]
  (tryc
    (let [uid (:uid sess)
          usr (get-user uid)]
      {:entry {:email (usr :login)}} ) ))

(def v-user-register (validation-set
                       (presence-of :email)
                       (presence-of :password)
                       (length-of :password :within (range 5 51))
                       (format-of :email :format #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")))

(defn handler-user-register 
  [{req :request json :json :as ctx}]
  (tryc
    (let [{e :email p :password} json
          usr (get-user-by-email e)] 
      (if usr
        {:err "Email already exists"}
        {:result (create-user 
                   {:id (uuid) 
                    :login e 
                    :password (cluser/bcrypt p)})}))))

(defn check-user-access
  "evaluates access to the route and verb"
  [uid]
  (when-let [actions (get-all-actions)]
    
    )
  )

(defn check-user 
  [{req :request}]
  (try
    (when-let [hval (get-in req [:headers "authorization"])]
      (when-let [sess (get-session hval)]
        {:current-session sess} ))
    (catch Exception e nil) ))

