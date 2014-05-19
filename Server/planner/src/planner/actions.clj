(ns planner.actions
  (:use planner.util
        cheshire.core
        clojure.walk
        planner.models.schema
        planner.services.validation
        planner.services.users
        planner.services.projects
        )
  (:require
    [compojure.route :as route]
    [clauth.endpoints :as ep ]
    [clojure.java.io :as io]
    [liberator.core :refer [resource defresource]]
    [liberator.representation :refer [ring-response]]))

(defn validate-data [ctx validator data]
  (when (nil? (:err ctx))
    (let [v (validator data)]
      (if (empty? v)
        [false {:json data}]
        [true {:err v}]) )))

(defn body-as-string [ctx]
  (if-let [body (get-in ctx [:request :body])]
    (condp instance? body
      String body
      (slurp (io/reader body)))))

(defn body-as-json
  "transforms the json string to an array
  with first element true if successfull or
  false if it gets an exception"
  [ctx]
  (try
    [true
     (keywordize-keys (parse-string (body-as-string ctx)))]
    (catch Exception e
      [false
       {:er (.getMessage e)}] )))

(defn is-valid [ctx validator]
  (let [[v j] (body-as-json ctx)]
    (if v (validate-data ctx validator j) j)))

(defn ret-handler [ctx]
  (if (:er ctx)
    (ring-response 
      {
       :status 500
       :body (generate-string {:er (:er ctx) :ec 0})
       :headers {"Content-Type" "application/json"} })
    {:data (:data ctx)}) )

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

(defresource action-user 
  :allowed-methods [:get :put :post]
  :authorized? 
  (fn[ctx] 
    (if (not= (get-in ctx [:request :request-method]) :post) 
      (check-user ctx)
      true))
  :available-media-types ["application/json"]
  :malformed? 
  (fn[ctx] 
    (if(not= (get-in ctx [:request :request-method]) :get) 
      (is-valid ctx v-user)))
  :exists? 
  (fn[ctx]
    (if-let [uid (get-in ctx [:request :params :id])]
      (get-by-id 
        users 
        (if uid 
          uid 
          (get-in ctx [:current-session :user :id])))))
  :put! handler-user-update
  :post! handler-user-create
  :respond-with-entity? true
  :handle-ok handler-user-get
  :handle-created ret-handler
  )
  

(defresource action-projects [id]
  :allowed-methods [:get :put :post]
  :authorized? check-user
  :available-media-types ["application/json"]
  :malformed? 
  (fn[ctx] 
    (if(not= (get-in ctx [:request :request-method]) :get) 
      (is-valid ctx v-project)))
  :exists? 
  (fn[ctx]
    (if(not= (get-in ctx [:request :request-method]) :post) 
      (handler-projects-get ctx)))
  :post! handler-project-save
  :put! handler-project-save
  :respond-with-entity? true
  ;:new? false
  :handle-created ret-handler
  :handle-ok ret-handler)

(defresource action-resources
  :allowed-methods [:get :put :post]
  :authorized? check-user
  :available-media-types ["application/json"]
  :exists? handler-resources-get
  ;:put! handler-put-save
  :respond-with-entity? true
  ;:new? false
  :handle-ok ret-handler)

