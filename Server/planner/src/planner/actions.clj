(ns planner.actions
  (:use planner.util
        planner.services.impl)
  (:require 
    [compojure.route :as route]
    [clauth.endpoints :as ep ]
    [liberator.core :refer [resource defresource]]
    [liberator.representation :refer [ring-response]] ))

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
  :allowed-methods [:get :post :put]
  :available-media-types ["application/json"]
  :exists? #(handler-user-get % id)
  :post! #(handler-user-save % nil)
  :put! #(handler-user-save % id)
  :new? #(and (nil? id) (contains? % ::user))
  :respond-with-entity? true
  :handle-created #(do (prn %) (::user %))
  :handle-ok ::entry)


