(ns planner.actions
  (:use planner.util
        cheshire.core
        clojure.walk
        planner.services.impl)
  (:require 
    [compojure.route :as route]
    [clauth.endpoints :as ep ]
    [clojure.java.io :as io]
    [liberator.core :refer [resource defresource]]
    [liberator.representation :refer [ring-response]] ))

(defn validate-data [ctx validator data]
  (when (nil? (:err ctx)) 
    (let [v (validator data)]
      (if (empty? v)
        [false] 
        [true {:err v}]) )))

(defn body-as-string [ctx]
  (if-let [body (get-in ctx [:request :body])]
    (condp instance? body
      java.lang.String body
      (slurp (io/reader body)))))

(defn body-as-json [ctx]
  (try
    [true (keywordize-keys (parse-string (body-as-string ctx)))] 
    (catch Exception e
      [false {:err (.getMessage e)}] )))

(defn is-valid [ctx validator] 
  (let [[v j] (body-as-json ctx)] 
    (if v (validate-data ctx validator j) j)))

(defn ret-handler [ctx] 
  (if (:err ctx) 
    {:err (:err ctx)}
    {:result (:result ctx)}) )

(defresource action-login
  :allowed-methods [:post :get]
  :available-media-types ["text/html"] 
  :handle-ok #(ring-response (handler-login-get %))
  :post! handler-login-post
  :post-redirect? false
  :handle-created #(ring-response (login-redirect %)))

(defresource action-logout
  :allowed-methods [:get]
  :available-media-types ["text/html"] 
  :handle-ok handler-logout)

(defresource action-token
  :allowed-methods [:get]
  :available-media-types ["text/html"] 
  :handle-ok #(ring-response (handler-token %)))

(defresource action-user [id]
  :allowed-methods [:get :put]
  :authorized? check-user
  :available-media-types ["application/json"]
  :exists? #(handler-user-get % id)
  :put! #(handler-user-save % id)
  :respond-with-entity? true
  :new? false
  :handle-ok :entry)

(defresource action-register 
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :malformed? #(is-valid % v-user-register)
  :post! #(handler-user-register %)
  :handle-malformed #(generate-string (:err %))
  :handle-created #(ret-handler %))
