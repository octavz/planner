(ns planner.services.impl
  (:use planner.repos.sql
        validateur.validation
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
  ((ep/login-handler {:client web-client-id}) req))

(defn handler-login-post
  "handle login and redirect in case of success"
  [{req :request}]
  (let [res (oauth-login req)
        at (-> res :session :access_token) ]
    {::access-token at}) )

(defn login-redirect 
  "doc-string"
  [ctx]
  (if-let [at (::access-token ctx)]
    (-> (rutil/redirect "/") 
      (rutil/set-cookie :access_token (::access-token ctx)))
    (rutil/redirect "/login?retry")))

(defn handler-token
  "oauth2 token handler"
  [{req :request}]
  (ep/token-handler req))

(defn handler-logout
  "oauth2 logout handler"
  [{req :request}]
  (comment "do logout"))

(defn handler-user-save 
  "creates or updates user"
  [{req :request} id]
  (prn (str "post " id))
  {::entry {:id (if (nil? id) "post-id" (str "put-id" id)) :name "post"}})

(defn handler-user-get
  "get user by id"
  [{req :request} id]
  {::entry {:id id :name "get"}})

(def v-user-register (validation-set
            (presence-of :email)
            (presence-of :password)
            (length-of :password :within (range 5 51))
            (format-of :email :format #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
            ))

(defn handler-user-register 
  [{req :request}]
    {:result "some value"}
  ;{:err "some error"} 
  )
